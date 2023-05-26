package beer.supportly.backend.database.entities

import jakarta.persistence.*

@Entity
data class TicketMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_to_message",
        joinColumns = [JoinColumn(name = "message_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val sender: UserEntity,
) {
    constructor() : this(null, "", UserEntity())
    constructor(
        content: String,
        sender: UserEntity
    ) : this(null, content, sender)
}