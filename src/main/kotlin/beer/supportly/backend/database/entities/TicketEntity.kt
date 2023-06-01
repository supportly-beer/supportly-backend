package beer.supportly.backend.database.entities

import jakarta.persistence.*

/**
 * Entity for the Ticket table
 *
 * @param id the id of the ticket
 * @param identifier the identifier of the ticket
 * @param title the title of the ticket
 * @param description the description of the ticket
 * @param createdAt the creation date of the ticket
 * @param closedAt the closing date of the ticket
 * @param updatedAt the update date of the ticket
 * @param creator the creator of the ticket
 * @param assignee the assignee of the ticket
 * @param state the state of the ticket
 * @param urgency the urgency of the ticket
 * @param messages the messages of the ticket
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 *
 * @see UserEntity
 * @see TicketState
 * @see TicketUrgency
 * @see TicketMessageEntity
 */
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