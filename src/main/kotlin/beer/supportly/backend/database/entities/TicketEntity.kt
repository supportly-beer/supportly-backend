package beer.supportly.backend.database.entities

import jakarta.persistence.*

@Entity
data class TicketEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val identifier: String,
    val title: String,
    val createdAt: Long,
    val closedAt: Long,
    val updatedAt: Long,

    @ManyToOne
    @JoinTable(
        name = "ticket_to_creator",
        joinColumns = [JoinColumn(name = "ticket_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val creator: UserEntity,

    @ManyToOne
    @JoinTable(
        name = "ticket_to_assignee",
        joinColumns = [JoinColumn(name = "ticket_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val assignee: UserEntity,

    @Enumerated(EnumType.STRING)
    val state: TicketState,

    @Enumerated(EnumType.STRING)
    val urgency: TicketUrgency,

    @OneToMany
    val messages: MutableList<TicketMessageEntity>
) {
    constructor() : this(
        null,
        "",
        "",
        -1,
        -1,
        -1,
        UserEntity(),
        UserEntity(),
        TicketState.OPEN,
        TicketUrgency.MINOR,
        mutableListOf()
    )

    constructor(
        identifier: String,
        title: String,
        createdAt: Long,
        closedAt: Long,
        updatedAt: Long,
        creator: UserEntity,
        assignee: UserEntity,
        state: TicketState,
        urgency: TicketUrgency,
        messages: MutableList<TicketMessageEntity>
    ) : this(null, identifier, title, createdAt, closedAt, updatedAt, creator, assignee, state, urgency, messages)
}