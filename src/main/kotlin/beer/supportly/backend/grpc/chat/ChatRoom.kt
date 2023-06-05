package beer.supportly.backend.grpc.chat

import beer.supportly.chat.TicketChat
import io.grpc.stub.StreamObserver

/**
 * ChatRoom is a data class that holds the information about a chat room.
 *
 * @property roomId The ID of the chat room.
 * @property users A map of the users in the chat room.
 * @property messages A list of the messages in the chat room.
 */
data class ChatRoom(
    val roomId: String,
    val users: MutableMap<Long, StreamObserver<TicketChat.ChatMessage>>,
    val messages: MutableList<TicketChat.ChatMessage>
)