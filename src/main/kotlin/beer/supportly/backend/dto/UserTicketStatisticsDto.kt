package beer.supportly.backend.dto

data class UserTicketStatisticsDto(
    val yourTicketsCreated: Int,
    val yourTicketsClosed: Int,
    val yourTicketsOpen: Int,
    val averageTimePerTicket: Long
)
