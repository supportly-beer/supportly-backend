package beer.supportly.backend.dto

/**
 * DTO for the creation of a ticket
 *
 * @property identifier the identifier of the ticket
 *
 * @constructor constructor with all values
 * @constructor constructor with no values where default values are set
 * @constructor constructor with only required values
 */
data class CreatedTicketDto(
    val identifier: String
)
