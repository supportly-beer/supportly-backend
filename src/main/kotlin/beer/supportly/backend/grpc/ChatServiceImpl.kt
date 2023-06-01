package beer.supportly.backend.grpc

import beer.supportly.backend.grpc.chat.ChatRoom
import beer.supportly.backend.service.TicketService
import beer.supportly.backend.service.UserService
import beer.supportly.chat.ChatServiceGrpc
import beer.supportly.chat.TicketChat
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class ChatServiceImpl(
    private val ticketService: TicketService,
    private val userService: UserService
) : ChatServiceGrpc.ChatServiceImplBase() {

    private val chatRooms: MutableMap<String, ChatRoom> = mutableMapOf()

    override fun joinChatRoom(
        joinRoomRequest: TicketChat.JoinRoomRequest,
        responseObserver: StreamObserver<TicketChat.ChatMessage>
    ) {
        val ticketEntity = ticketService.getOriginalTicket(joinRoomRequest.roomId)

        if (ticketEntity.isPresent) {
            val chatRoom = chatRooms.getOrPut(joinRoomRequest.roomId) {
                ChatRoom(
                    joinRoomRequest.roomId,
                    mutableMapOf(),
                    ticketEntity.get().messages.stream().map {
                        TicketChat.ChatMessage.newBuilder()
                            .setRoomId(joinRoomRequest.roomId)
                            .setSenderId(it.sender.id!!)
                            .setSenderDisplayName(it.sender.firstName + " " + it.sender.lastName)
                            .setMessage(it.content)
                            .setTimestamp(it.timestamp)
                            .build()
                    }.toList()
                )
            }

            chatRoom.users[joinRoomRequest.userId] = responseObserver

            chatRoom.messages.forEach {
                responseObserver.onNext(it)
            }
        } else {
            responseObserver.onCompleted()
        }
    }

    override fun sendMessage(
        chatMessage: TicketChat.ChatMessage,
        responseObserver: StreamObserver<Empty>
    ) {
        val chatRoom = chatRooms[chatMessage.roomId]

        val ticketEntity = ticketService.getOriginalTicket(chatMessage.roomId)
        val userEntity = userService.getOriginalUser(chatMessage.senderId)

        if (chatRoom != null && ticketEntity.isPresent && userEntity.isPresent) {
            chatRoom.messages.add(chatMessage)

            chatRoom.users.forEach {
                it.value.onNext(chatMessage)
            }

            ticketService.addMessage(ticketEntity.get(), userEntity.get(), chatMessage.timestamp, chatMessage.message)
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }

    override fun leaveChatroom(
        leaveRoomRequest: TicketChat.LeaveRoomRequest,
        responseObserver: StreamObserver<Empty>
    ) {
        val chatRoom = chatRooms[leaveRoomRequest.roomId]

        if (chatRoom != null) {
            chatRoom.users[leaveRoomRequest.userId]?.onCompleted()
            chatRoom.users.remove(leaveRoomRequest.userId)
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }
}