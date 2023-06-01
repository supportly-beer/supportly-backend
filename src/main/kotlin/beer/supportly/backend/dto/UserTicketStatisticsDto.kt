package beer.supportly.backend.dto

/**
 * Data transfer object for user ticket statistics.
 *
 * @property yourTicketsCreated The yourTicketsCreated of the user ticket statistics.
 * @property yourTicketsClosed The yourTicketsClosed of the user ticket statistics.
 * @property yourTicketsOpen The yourTicketsOpen of the user ticket statistics.
 * @property averageTimePerTicket The averageTimePerTicket of the user ticket statistics.
 *
 * @constructor constructor with all values
 */
data class UserTicketStatisticsDto(
    val yourTicketsCreated: Int,
    val yourTicketsClosed: Int,
    val yourTicketsOpen: Int,
    val averageTimePerTicket: Long
)
