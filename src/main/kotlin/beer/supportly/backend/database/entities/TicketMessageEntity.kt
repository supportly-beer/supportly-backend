package beer.supportly.backend.database.entities

import jakarta.persistence.*

/**
 * Entity for the TicketMessage table
 *
 * @property id the id of the ticket message
 * @property content the content of the ticket message
 * @property timestamp the timestamp of the ticket message
 * @property sender the sender of the ticket message
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 *
 * @see UserEntity
 */
@Entity
data class TicketMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val content: String,
    val timestamp: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_to_message",
        joinColumns = [JoinColumn(name = "message_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val sender: UserEntity,
) {
    constructor() : this(null, "", -1L, UserEntity())
    constructor(
        content: String,
        timestamp: Long,
        sender: UserEntity
    ) : this(null, content, timestamp, sender)
}