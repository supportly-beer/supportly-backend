package beer.supportly.backend.dto

import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.TicketUrgency

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
