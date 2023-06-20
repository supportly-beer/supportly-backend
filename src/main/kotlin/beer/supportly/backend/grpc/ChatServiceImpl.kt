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
import java.util.stream.Collectors

/**
 * ChatServiceImpl is the implementation of the ChatServiceGrpc.ChatServiceImplBase
 *
 * @property ticketService the ticket service
 * @property userService the user service
 */
@Service
@Transactional
class ChatServiceImpl(
    private val ticketService: TicketService,
    private val userService: UserService
) : ChatServiceGrpc.ChatServiceImplBase() {

    private val chatRooms: MutableMap<String, ChatRoom> = mutableMapOf()

    /**
     * joinChatRoom is the implementation of the joinChatRoom method of the ChatServiceGrpc.ChatServiceImplBase
     *
     * @param joinRoomRequest the join room request
     * @param responseObserver the response observer
     */
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
                            .setSenderProfilePictureUrl(it.sender.profilePictureUrl)
                            .setMessage(it.content)
                            .setTimestamp(it.timestamp)
                            .build()
                    }.collect(Collectors.toList())
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

    /**
     * sendMessage is the implementation of the sendMessage method of the ChatServiceGrpc.ChatServiceImplBase
     *
     * @param chatMessage the chat message
     * @param responseObserver the response observer
     */
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

    /**
     * leaveChatroom is the implementation of the leaveChatroom method of the ChatServiceGrpc.ChatServiceImplBase
     *
     * @param leaveRoomRequest the leave room request
     * @param responseObserver the response observer
     */
    override fun leaveChatroom(
        leaveRoomRequest: TicketChat.LeaveRoomRequest,
        responseObserver: StreamObserver<Empty>
    ) {
        val chatRoom = chatRooms[leaveRoomRequest.roomId]

        if (chatRoom != null) {
            chatRoom.users[leaveRoomRequest.userId]?.onCompleted()
            chatRoom.users.remove(leaveRoomRequest.userId)

            if (chatRoom.users.isEmpty()) {
                chatRooms.remove(leaveRoomRequest.roomId)
            }
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }
}