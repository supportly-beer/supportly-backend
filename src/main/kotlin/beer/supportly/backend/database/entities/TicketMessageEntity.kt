package beer.supportly.backend.database.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class TicketMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val content: String,
    val senderId: Long,
) {
    constructor() : this(null, "", -1)
    constructor(
        content: String,
        senderId: Long
    ) : this(null, content, senderId)
}