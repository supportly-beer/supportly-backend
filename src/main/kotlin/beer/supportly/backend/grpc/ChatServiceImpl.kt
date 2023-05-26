package beer.supportly.backend.grpc

import beer.supportly.chat.ChatServiceGrpc
import beer.supportly.chat.TicketChat
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service

@Service
class ChatServiceImpl : ChatServiceGrpc.ChatServiceImplBase() {

    private val chatRooms: MutableMap<String, Pair<MutableList<Pair<String, StreamObserver<TicketChat.ChatMessage>>>,
            MutableList<TicketChat.ChatMessage>>> = mutableMapOf()

    override fun joinChatRoom(
        joinRoomRequest: TicketChat.JoinRoomRequest,
        responseObserver: StreamObserver<TicketChat.ChatMessage>
    ) {
        val roomId = joinRoomRequest.roomId
        val userId = joinRoomRequest.userId
        val userDisplayName = joinRoomRequest.userDisplayName

        val chatRoom = chatRooms.getOrPut(roomId) {
            Pair(mutableListOf(), mutableListOf())
        }

        val joinMessage = TicketChat.ChatMessage.newBuilder()
            .setRoomId(roomId)
            .setSenderDisplayName("System")
            .setMessage("join_$userId")
            .setTimestamp(System.currentTimeMillis().toString())
            .build()

        chatRoom.second.add(joinMessage)

        chatRoom.first.forEach { it.second.onNext(joinMessage) }
        chatRoom.first.add(Pair(userId, responseObserver))

        chatRoom.second.stream().forEach {
            responseObserver.onNext(it)
        }
    }

    override fun sendMessage(
        chatMessage: TicketChat.ChatMessage,
        responseObserver: StreamObserver<Empty>
    ) {
        val chatRoom = chatRooms[chatMessage.roomId]

        if (chatRoom != null) {
            chatRoom.second.add(chatMessage)
            chatRoom.first.forEach {
                it.second.onNext(chatMessage)
            }
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }

    override fun leaveChatroom(
        leaveRoomRequest: TicketChat.LeaveRoomRequest,
        responseObserver: StreamObserver<Empty>
    ) {
        val roomId = leaveRoomRequest.roomId
        val userId = leaveRoomRequest.userId

        val chatRoom = chatRooms[roomId]

        if (chatRoom != null) {
            val leaveMessage = TicketChat.ChatMessage.newBuilder()
                .setRoomId(roomId)
                .setSenderDisplayName("System")
                .setMessage("leave_$userId")
                .setTimestamp(System.currentTimeMillis().toString())
                .build()

            chatRoom.second.add(leaveMessage)

            chatRoom.first.forEach { it.second.onNext(leaveMessage) }
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }
}