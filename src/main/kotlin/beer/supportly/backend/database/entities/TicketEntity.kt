package beer.supportly.backend.database.entities

import jakarta.persistence.*

/**
 * Entity for the Ticket table
 *
 * @property id the id of the ticket
 * @property identifier the identifier of the ticket
 * @property title the title of the ticket
 * @property description the description of the ticket
 * @property createdAt the creation date of the ticket
 * @property closedAt the closing date of the ticket
 * @property updatedAt the update date of the ticket
 * @property creator the creator of the ticket
 * @property assignee the assignee of the ticket
 * @property state the state of the ticket
 * @property urgency the urgency of the ticket
 * @property messages the messages of the ticket
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
    var closedAt: Long? = null,
    var updatedAt: Long? = null,

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