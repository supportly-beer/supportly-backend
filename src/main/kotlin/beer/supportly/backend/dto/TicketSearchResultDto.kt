package beer.supportly.backend.dto

/**
 * Data transfer object for ticket search result.
 *
 * @property id The id of the ticket.
 * @property identifier The identifier of the ticket.
 * @property title The title of the ticket.
 * @property description The description of the ticket.
 *
 * @constructor constructor with all values
 */
data class TicketSearchResultDto(
    val id: Long,
    val identifier: String,
    val title: String,
    val description: String
)