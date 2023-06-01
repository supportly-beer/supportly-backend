package beer.supportly.backend.dto

/**
 * Data transfer object for creating a ticket.
 *
 * @property title The title of the ticket.
 * @property description The description of the ticket.
 *
 * @constructor constructor with all values
 */
data class CreateTicketDto(
    val title: String,
    val description: String
)