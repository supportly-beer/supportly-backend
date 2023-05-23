package beer.supportly.backend.grpc

import beer.supportly.protochat.ChatServiceGrpc
import beer.supportly.protochat.TicketChat
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service

@Service
class ChatServiceImpl : ChatServiceGrpc.ChatServiceImplBase() {

    private val chatRooms: MutableMap<String, Pair<MutableList<StreamObserver<TicketChat.ChatMessage>>,
            MutableList<TicketChat.ChatMessage>>> = mutableMapOf()

    override fun joinChatRoom(
        joinRoomRequest: TicketChat.JoinRoomRequest,
        responseObserver: StreamObserver<TicketChat.ChatMessage>
    ) {
        val roomId = joinRoomRequest.roomId
        val userId = joinRoomRequest.userId
        val username = joinRoomRequest.username

        val chatRoom = chatRooms.getOrPut(roomId) {
            Pair(mutableListOf(), mutableListOf())
        }

        val joinMessage = TicketChat.ChatMessage.newBuilder()
            .setRoomId(roomId)
            .setSender("System")
            .setMessage("join_$username")
            .setTimestamp(System.currentTimeMillis().toString())
            .build()

        chatRoom.second.add(joinMessage)

        chatRoom.first.forEach { it.onNext(joinMessage) }
        chatRoom.first.add(responseObserver)

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
                it.onNext(chatMessage)
            }
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }
}