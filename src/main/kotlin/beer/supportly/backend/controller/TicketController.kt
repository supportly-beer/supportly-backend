package beer.supportly.backend.controller

import beer.supportly.backend.dto.*
import beer.supportly.backend.service.TicketService
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller for the ticket endpoints
 *
 * @property ticketService contains the ticket service
 *
 * @see beer.supportly.backend.service.TicketService
 */
@RestController
@RequestMapping("/ticket")
class TicketController(
    private val ticketService: TicketService
) {

    /**
     * Endpoint for statistics of an agent
     *
     * @param token contains the token
     * @param startDate contains the start date
     * @param endDate contains the end date
     *
     * @return AgentTicketStatisticsDto contains the statistics
     *
     * @see beer.supportly.backend.dto.AgentTicketStatisticsDto
     */
    @GetMapping("/statistics/agent")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getAgentStatistics(
        @RequestHeader("Authorization") token: String,
        @RequestParam(name = "startDate", required = false) startDate: Long,
        @RequestParam(name = "endDate", required = false) endDate: Long
    ): ResponseEntity<AgentTicketStatisticsDto> {
        return ResponseEntity.ok(
            ticketService.getAgentStatistics(
                token.substring("Bearer ".length), startDate, endDate
            )
        )
    }

    /**
     * Endpoint for statistics of a user
     *
     * @param token contains the token
     * @param startDate contains the start date
     * @param endDate contains the end date
     *
     * @return UserTicketStatisticsDto contains the statistics
     *
     * @see beer.supportly.backend.dto.UserTicketStatisticsDto
     */
    @GetMapping("/statistics/user")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getUserStatistics(
        @RequestHeader("Authorization") token: String,
        @RequestParam(name = "startDate", required = false) startDate: Long,
        @RequestParam(name = "endDate", required = false) endDate: Long
    ): ResponseEntity<UserTicketStatisticsDto> {
        return ResponseEntity.ok(ticketService.getUserStatistics(token.substring("Bearer ".length), startDate, endDate))
    }

    /**
     * Endpoint to get a ticket
     *
     * @param identifier contains the identifier
     *
     * @return TicketDto contains the data of the ticket
     *
     * @see beer.supportly.backend.dto.TicketDto
     */
    @GetMapping("{identifier}")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getTicket(@PathVariable("identifier") identifier: String): ResponseEntity<TicketDto> {
        return ResponseEntity.ok(ticketService.getTicket(identifier))
    }

    /**
     * Endpoint to get all tickets
     *
     * @param start contains the start
     * @param limit contains the limit
     *
     * @return List<TicketDto> contains the data of the tickets
     *
     * @see beer.supportly.backend.dto.TicketDto
     */
    @GetMapping("/all")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getAllTickets(
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<TicketDto>> {
        return ResponseEntity.ok(ticketService.getAllTickets(start, limit))
    }

    /**
     * Endpoint to create a ticket
     *
     * @param token contains the token
     * @param createTicketDto contains the data of the ticket
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @see beer.supportly.backend.dto.CreateTicketDto
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping
    fun createTicket(
        @RequestHeader("Authorization") token: String,
        @RequestBody createTicketDto: CreateTicketDto
    ): ResponseEntity<OperationSuccessDto> {
        ticketService.createTicket(token.substring("Bearer ".length), createTicketDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to assign a ticket
     *
     * @param token contains the token
     * @param identifier contains the identifier
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("{identifier}/assign")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun assignTicket(
        @RequestHeader("Authorization") token: String,
        @PathVariable("identifier") identifier: String
    ): ResponseEntity<OperationSuccessDto> {
        ticketService.assignTicket(token.substring("Bearer ".length), identifier)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to update a ticket
     *
     * @param identifier contains the identifier
     * @param updateTicketDto contains the data to update the ticket
     *
     * @return OperationSuccessDto contains the operation success
     *
     * @see beer.supportly.backend.dto.UpdateTicketDto
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @PostMapping("{identifier}/update")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun updateTicket(
        @PathVariable("identifier") identifier: String,
        @RequestBody updateTicketDto: UpdateTicketDto
    ): ResponseEntity<OperationSuccessDto> {
        ticketService.updateTicket(identifier, updateTicketDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    /**
     * Endpoint to get all tickets owned by the authenticated user
     *
     * @param token contains the token
     * @param start contains the start
     * @param limit contains the limit
     *
     * @return List<TicketDto> contains the data of the tickets
     *
     * @see beer.supportly.backend.dto.TicketDto
     */
    @GetMapping("/my")
    fun getMyTickets(
        @RequestHeader("Authorization") token: String,
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<TicketDto>> {
        return ResponseEntity.ok(ticketService.getMyTickets(token.substring("Bearer ".length), start, limit))
    }

    /**
     * Endpoint to get a ticket owned by the authenticated user
     *
     * @param token contains the token
     * @param identifier contains the identifier
     *
     * @return TicketDto contains the data of the ticket
     *
     * @see beer.supportly.backend.dto.TicketDto
     */
    @GetMapping("/my/{identifier}")
    fun getMyTicket(
        @RequestHeader("Authorization") token: String,
        @PathVariable("identifier") identifier: String
    ): ResponseEntity<TicketDto> {
        return ResponseEntity.ok(ticketService.getMyTicket(token.substring("Bearer ".length), identifier))
    }
}