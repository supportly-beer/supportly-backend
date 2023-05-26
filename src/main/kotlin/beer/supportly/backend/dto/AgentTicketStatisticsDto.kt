package beer.supportly.backend.dto

data class AgentTicketStatisticsDto(
    val globalTicketsOpen: Int,
    val yourTicketsOpen: Int,
    val customerAccounts: Int,
    val averageTimePerTicket: Long
)
