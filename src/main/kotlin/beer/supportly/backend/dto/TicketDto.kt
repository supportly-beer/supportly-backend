package beer.supportly.backend.dto

import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.TicketUrgency

/**
 * Data transfer object for ticket.
 *
 * @property identifier The identifier of the ticket.
 * @property title The title of the ticket.
 * @property description The description of the ticket.
 * @property createdAt The creation date of the ticket.
 * @property closedAt The closing date of the ticket.
 * @property updatedAt The update date of the ticket.
 * @property creator The creator of the ticket.
 * @property assignee The assignee of the ticket.
 * @property state The state of the ticket.
 * @property urgency The urgency of the ticket.
 *
 * @constructor constructor with all values
 *
 * @see beer.supportly.backend.dto.UserDto
 * @see beer.supportly.backend.database.entities.TicketState
 * @see beer.supportly.backend.database.entities.TicketUrgency
 */
data class TicketDto(
    val identifier: String,
    val title: String,
    val description: String,
    val createdAt: Long,
    val closedAt: Long?,
    val updatedAt: Long?,
    val creator: UserDto,
    val assignee: UserDto?,
    val state: TicketState,
    val urgency: TicketUrgency,
)
