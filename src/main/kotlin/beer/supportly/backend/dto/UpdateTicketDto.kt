package beer.supportly.backend.dto

import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.TicketUrgency

data class UpdateTicketDto(
    val ticketUrgency: TicketUrgency?,
    val ticketState: TicketState?
)
