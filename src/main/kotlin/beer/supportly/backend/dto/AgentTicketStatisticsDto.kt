package beer.supportly.backend.dto

/**
 * Data transfer object for agent ticket statistics.
 *
 * @property globalTicketsOpen The number of tickets open in the system.
 * @property yourTicketsOpen The number of tickets open assigned to the agent.
 * @property customerAccounts The number of customer accounts in the system.
 * @property averageTimePerTicket The average time per ticket in the system.
 *
 * @constructor constructor with all values
 */
data class AgentTicketStatisticsDto(
    val globalTicketsOpen: Int,
    val yourTicketsOpen: Int,
    val customerAccounts: Int,
    val averageTimePerTicket: Long
)
