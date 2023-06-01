package beer.supportly.backend.grpc.chat

import beer.supportly.chat.TicketChat
import io.grpc.stub.StreamObserver

data class ChatRoom(
    val roomId: String,
    val users: MutableMap<Long, StreamObserver<TicketChat.ChatMessage>>,
    val messages: MutableList<TicketChat.ChatMessage>
)