package beer.supportly.backend.service

import beer.supportly.backend.database.entities.TicketEntity
import beer.supportly.backend.database.entities.TicketState
import beer.supportly.backend.database.entities.TicketUrgency
import beer.supportly.backend.database.repositories.TicketRepository
import beer.supportly.backend.dto.*
import beer.supportly.backend.dto.mapper.TicketDtoMapper
import beer.supportly.backend.exception.BackendException
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import kotlin.math.roundToLong

@Service
@Transactional
class TicketService(
    private val userService: UserService,
    private val ticketRepository: TicketRepository,
    private val ticketDtoMapper: TicketDtoMapper
) {
    fun getAgentStatistics(token: String, startDate: Long?, endDate: Long?): AgentTicketStatisticsDto {
        val userEntity = userService.getOriginalUserFromToken(token)

        if (startDate == null || endDate == null) {
            val globalTicketsOpen = ticketRepository.findAllByState(TicketState.OPEN).get().count()
            val yourTicketsOpen = ticketRepository.findAllByAssigneeAndState(userEntity, TicketState.OPEN).get().count()
            val yourTicketsAssigned =
                ticketRepository.findAllByAssigneeAndState(userEntity, TicketState.ASSIGNED).get().count()
            val yourTicketsClosed = ticketRepository.findAllByAssigneeAndState(userEntity, TicketState.FINISHED).get()

            val averageResponseTime = yourTicketsClosed.stream()
                .map { it.closedAt!! - it.createdAt }
                .collect(Collectors.toList()).average()

            return AgentTicketStatisticsDto(
                globalTicketsOpen,
                yourTicketsOpen + yourTicketsAssigned,
                yourTicketsClosed.count(),
                averageResponseTime.roundToLong()
            )
        }

        val globalTicketsOpen =
            ticketRepository.findAllByStateAndCreatedAtBetween(TicketState.OPEN, startDate, endDate).get().count()
        val yourTicketsOpen = ticketRepository.findAllByAssigneeAndStateAndCreatedAtBetween(
            userEntity,
            TicketState.OPEN,
            startDate,
            endDate,
        ).get().count()
        val yourTicketsAssigned = ticketRepository.findAllByAssigneeAndStateAndCreatedAtBetween(
            userEntity,
            TicketState.ASSIGNED,
            startDate,
            endDate,
        ).get().count()
        val yourTicketsClosed = ticketRepository.findAllByAssigneeAndStateAndClosedAtBetween(
            userEntity,
            TicketState.FINISHED,
            startDate,
            endDate
        ).get()

        var averageResponseTime = yourTicketsClosed.stream()
            .map { it.closedAt!! - it.createdAt }
            .collect(Collectors.toList()).average()

        if (averageResponseTime.isNaN()) {
            averageResponseTime = -1.0
        }

        return AgentTicketStatisticsDto(
            globalTicketsOpen,
            yourTicketsOpen + yourTicketsAssigned,
            yourTicketsClosed.count(),
            averageResponseTime.roundToLong()
        )
    }

    fun getUserStatistics(token: String, startDate: Long?, endDate: Long?): UserTicketStatisticsDto {
        val userEntity = userService.getOriginalUserFromToken(token)

        if (startDate == null || endDate == null) {
            val yourTicketsCreated = ticketRepository.findAllByCreator(userEntity).get().count()
            val yourTicketsOpen = ticketRepository.findAllByCreatorAndState(userEntity, TicketState.OPEN).get().count()
            val yourTicketsAssigned =
                ticketRepository.findAllByCreatorAndState(userEntity, TicketState.ASSIGNED).get().count()
            val yourTicketsClosed = ticketRepository.findAllByCreatorAndState(userEntity, TicketState.FINISHED).get()

            val averageResponseTime = yourTicketsClosed.stream()
                .map { it.closedAt!! - it.createdAt }
                .collect(Collectors.toList()).average()

            return UserTicketStatisticsDto(
                yourTicketsCreated,
                yourTicketsClosed.count(),
                yourTicketsOpen + yourTicketsAssigned,
                averageResponseTime.roundToLong()
            )
        }

        val yourTicketsCreated =
            ticketRepository.findAllByCreatorAndCreatedAtBetween(userEntity, startDate, endDate).get().count()
        val yourTicketsClosed =
            ticketRepository.findAllByCreatorAndClosedAtBetween(userEntity, startDate, endDate).get()
        val yourTicketsOpen = ticketRepository.findAllByCreatorAndStateAndCreatedAtBetween(
            userEntity,
            TicketState.OPEN,
            startDate,
            endDate
        ).get().count()
        val yourTicketsAssigned = ticketRepository.findAllByCreatorAndStateAndCreatedAtBetween(
            userEntity,
            TicketState.ASSIGNED,
            startDate,
            endDate
        ).get().count()

        var averageResponseTime = yourTicketsClosed.stream()
            .map { it.closedAt!! - it.createdAt }
            .collect(Collectors.toList()).average()

        if (averageResponseTime.isNaN()) {
            averageResponseTime = -1.0
        }

        return UserTicketStatisticsDto(
            yourTicketsCreated,
            yourTicketsClosed.count(),
            yourTicketsOpen + yourTicketsAssigned,
            averageResponseTime.roundToLong()
        )
    }

    fun getTicket(identifier: String): TicketDto {
        return ticketRepository.findByIdentifier(identifier)
            .map(ticketDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }
    }

    fun getAllTickets(start: Int, limit: Int): List<TicketDto> {
        return ticketRepository.findAll(PageRequest.of(start, limit)).stream()
            .map(ticketDtoMapper)
            .collect(Collectors.toList())
    }

    fun createTicket(token: String, createTicketDto: CreateTicketDto) {
        val creator = userService.getOriginalUserFromToken(token)
        val ticketCount = ticketRepository.count()

        val ticketEntity = TicketEntity(
            "TICKET-${ticketCount + 1}",
            createTicketDto.title,
            createTicketDto.description,
            System.currentTimeMillis(),
            creator,
            TicketState.OPEN,
            TicketUrgency.NORMAL,
            mutableListOf()
        )

        ticketRepository.save(ticketEntity)
    }

    fun getMyTickets(token: String, start: Int, limit: Int): List<TicketDto> {
        val userEntity = userService.getOriginalUserFromToken(token)
        var tickets: MutableList<TicketDto> = mutableListOf()

        when (userEntity.role.name) {
            "ROLE_ADMINISTRATOR", "ROLE_AGENT" -> {
                tickets = ticketRepository.findAllByAssignee(userEntity, PageRequest.of(start, limit)).stream()
                    .map(ticketDtoMapper)
                    .collect(Collectors.toList())
            }

            "ROLE_USER" -> {
                tickets = ticketRepository.findAllByCreator(userEntity, PageRequest.of(start, limit)).stream()
                    .map(ticketDtoMapper)
                    .collect(Collectors.toList())
            }
        }

        return tickets
    }

    fun getMyTicket(token: String, identifier: String): TicketDto {
        val userEntity = userService.getOriginalUserFromToken(token)

        return ticketRepository.findByCreatorAndIdentifier(userEntity, identifier)
            .map(ticketDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }
    }

    fun assignTicket(token: String, identifier: String) {
        val userEntity = userService.getOriginalUserFromToken(token)
        val ticketEntity = ticketRepository.findByIdentifier(identifier)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }

        ticketEntity.assignee = userEntity
    }

    fun updateTicket(identifier: String, updateTicketDto: UpdateTicketDto) {
        val ticketEntity = ticketRepository.findByIdentifier(identifier)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }

        if (updateTicketDto.ticketState != null) ticketEntity.state = updateTicketDto.ticketState
        if (updateTicketDto.ticketUrgency != null) ticketEntity.urgency = updateTicketDto.ticketUrgency
    }
}