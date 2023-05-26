package beer.supportly.backend.database.entities

import jakarta.persistence.*

@Entity
data class TicketEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val identifier: String,
    val title: String,
    val description: String,
    val createdAt: Long,
    val closedAt: Long? = null,
    val updatedAt: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ticket_to_creator",
        joinColumns = [JoinColumn(name = "ticket_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val creator: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ticket_to_assignee",
        joinColumns = [JoinColumn(name = "ticket_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var assignee: UserEntity? = null,

    @Enumerated(EnumType.STRING)
    var state: TicketState,

    @Enumerated(EnumType.STRING)
    var urgency: TicketUrgency,

    @OneToMany
    val messages: MutableList<TicketMessageEntity>
) {
    constructor() : this(
        null,
        "",
        "",
        "",
        -1,
        -1,
        -1,
        UserEntity(),
        null,
        TicketState.OPEN,
        TicketUrgency.MINOR,
        mutableListOf()
    )

    constructor(
        identifier: String,
        title: String,
        description: String,
        createdAt: Long,
        creator: UserEntity,
        state: TicketState,
        urgency: TicketUrgency,
        messages: MutableList<TicketMessageEntity>
    ) : this(
        null,
        identifier,
        title,
        description,
        createdAt,
        -1,
        -1,
        creator,
        null,
        state,
        urgency,
        messages
    )
}