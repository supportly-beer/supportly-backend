package beer.supportly.backend.dto

import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.TicketUrgency

/**
 * Data transfer object for update ticket.
 *
 * @property ticketUrgency The ticketUrgency of the update ticket.
 * @property ticketState The ticketState of the update ticket.
 *
 * @constructor constructor with all values
 *
 * @see beer.supportly.backend.database.entities.TicketUrgency
 * @see beer.supportly.backend.database.entities.TicketState
 */
data class UpdateTicketDto(
    val ticketUrgency: TicketUrgency?,
    val ticketState: TicketState?
)
