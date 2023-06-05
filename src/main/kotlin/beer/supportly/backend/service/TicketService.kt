package beer.supportly.backend.service

import beer.supportly.backend.database.entities.*
import beer.supportly.backend.database.repositories.TicketMessageRepository
import beer.supportly.backend.database.repositories.TicketRepository
import beer.supportly.backend.dto.*
import beer.supportly.backend.dto.mapper.SearchResultMapper
import beer.supportly.backend.dto.mapper.TicketDtoMapper
import beer.supportly.backend.exception.BackendException
import com.meilisearch.sdk.Client
import com.meilisearch.sdk.SearchRequest
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import kotlin.math.roundToLong

/**
 * Service for handling tickets.
 *
 * @property userService Service for handling users.
 * @property ticketRepository Repository for handling tickets.
 * @property ticketMessageRepository Repository for handling ticket messages.
 * @property ticketDtoMapper Mapper for mapping ticket entities to ticket DTOs.
 * @property searchResultMapper Mapper for mapping search results to ticket DTOs.
 * @property searchClient Client for searching tickets.
 *
 * @see beer.supportly.backend.service.UserService
 * @see beer.supportly.backend.database.repositories.TicketRepository
 * @see beer.supportly.backend.database.repositories.TicketMessageRepository
 * @see beer.supportly.backend.dto.mapper.TicketDtoMapper
 * @see beer.supportly.backend.dto.mapper.SearchResultMapper
 */
@Service
@Transactional
class TicketService(
    private val userService: UserService,
    private val ticketRepository: TicketRepository,
    private val ticketMessageRepository: TicketMessageRepository,
    private val ticketDtoMapper: TicketDtoMapper,
    private val searchResultMapper: SearchResultMapper,
    private val searchClient: Client,
) {

    /**
     * This method is used to get the agent statistics.
     *
     * @param token The token of the user creating the ticket.
     * @param startDate The start date of the statistics.
     * @param endDate The end date of the statistics.
     *
     * @return The agent statistics.
     */
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

    /**
     * This method is used to get the user statistics.
     *
     * @param token The token of the user creating the ticket.
     * @param startDate The start date of the statistics.
     * @param endDate The end date of the statistics.
     *
     * @return The user statistics.
     */
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

    /**
     * This method is used get a ticket by its identifier.
     *
     * @param identifier The identifier of the ticket.
     *
     * @return The ticket dto.
     */
    fun getTicket(identifier: String): TicketDto {
        return this.getOriginalTicket(identifier)
            .map(ticketDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }
    }

    /**
     * This method is used to get the original ticket entity.
     *
     * @param identifier The identifier of the ticket.
     *
     * @return The ticket entity.
     */
    fun getOriginalTicket(identifier: String): Optional<TicketEntity> {
        return ticketRepository.findByIdentifier(identifier)
    }

    /**
     * This method is used to get all tickets.
     *
     * @param start The start of the tickets.
     * @param limit The limit of the tickets.
     *
     * @return The list of tickets.
     */
    fun getAllTickets(start: Int, limit: Int): List<TicketDto> {
        return ticketRepository.findAll(PageRequest.of(start, limit)).stream()
            .map(ticketDtoMapper)
            .collect(Collectors.toList())
    }

    /**
     * This method is used to add a message to a ticket.
     *
     * @param ticketEntity The ticket entity.
     * @param userEntity The user entity.
     * @param timestamp The timestamp of the message.
     * @param content The content of the message.
     */
    fun addMessage(ticketEntity: TicketEntity, userEntity: UserEntity, timestamp: Long, content: String) {
        val message = TicketMessageEntity(content, timestamp, userEntity)
        ticketMessageRepository.save(message)

        ticketEntity.messages.add(message)
    }

    /**
     * This method is used to create a ticket.
     *
     * @param token The token of the user creating the ticket.
     * @param createTicketDto The create ticket dto.
     */
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

        val json = """
            {
                "id": "${ticketEntity.id}",
                "identifier": "${ticketEntity.identifier}",
                "title": "${ticketEntity.title}",
                "description": "${ticketEntity.description}"
            }
            """

        val index = searchClient.index("tickets")
        index.addDocuments(json)
    }

    /**
     * This method is used to get my tickets
     *
     * @param token The token of the user.
     * @param start The start of the tickets.
     * @param limit The limit of the tickets.
     */
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

    /**
     * This method is used to get a ticket owned by me by its identifier.
     *
     * @param token The token of the user.
     * @param identifier The identifier of the ticket.
     *
     * @return The ticket dto.
     */
    fun getMyTicket(token: String, identifier: String): TicketDto {
        val userEntity = userService.getOriginalUserFromToken(token)

        return ticketRepository.findByCreatorAndIdentifier(userEntity, identifier)
            .map(ticketDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }
    }

    /**
     * This method is used to assign a ticket to a user.
     *
     * @param token The token of the user.
     * @param identifier The identifier of the ticket.
     */
    fun assignTicket(token: String, identifier: String) {
        val userEntity = userService.getOriginalUserFromToken(token)
        val ticketEntity = ticketRepository.findByIdentifier(identifier)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }

        ticketEntity.assignee = userEntity
    }

    /**
     * This method is used to update the state and the urgency of a ticket.
     *
     * @param identifier The identifier of the ticket.
     * @param updateTicketDto The update ticket dto.
     */
    fun updateTicket(identifier: String, updateTicketDto: UpdateTicketDto) {
        val ticketEntity = ticketRepository.findByIdentifier(identifier)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }

        if (updateTicketDto.ticketState != null) ticketEntity.state = updateTicketDto.ticketState
        if (updateTicketDto.ticketUrgency != null) ticketEntity.urgency = updateTicketDto.ticketUrgency
    }

    /**
     * This method is used to search for tickets.
     *
     * @param query The query to search for.
     * @param limit The limit of the search.
     *
     * @return The search result dto.
     */
    fun searchTickets(query: String, limit: Int): SearchResultDto {
        val index = searchClient.index("tickets")

        val searchRequest = SearchRequest.builder()
            .limit(limit)
            .build()

        searchRequest.setQuery(query)

        return Optional.of(index.search(searchRequest))
            .map(searchResultMapper)
            .orElseThrow { BackendException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while searching!") }
    }
}