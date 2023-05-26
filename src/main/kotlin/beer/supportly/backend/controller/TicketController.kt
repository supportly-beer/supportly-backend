package beer.supportly.backend.controller

import beer.supportly.backend.dto.*
import beer.supportly.backend.service.TicketService
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ticket")
class TicketController(
    private val ticketService: TicketService
) {

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

    @GetMapping("/statistics/user")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getUserStatistics(
        @RequestHeader("Authorization") token: String,
        @RequestParam(name = "startDate", required = false) startDate: Long,
        @RequestParam(name = "endDate", required = false) endDate: Long
    ): ResponseEntity<UserTicketStatisticsDto> {
        return ResponseEntity.ok(ticketService.getUserStatistics(token.substring("Bearer ".length), startDate, endDate))
    }

    @GetMapping("{identifier}")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getTicket(@PathVariable("identifier") identifier: String): ResponseEntity<TicketDto> {
        return ResponseEntity.ok(ticketService.getTicket(identifier))
    }

    @GetMapping("/all")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun getAllTickets(
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<TicketDto>> {
        return ResponseEntity.ok(ticketService.getAllTickets(start, limit))
    }

    @PostMapping
    fun createTicket(
        @RequestHeader("Authorization") token: String,
        @RequestBody createTicketDto: CreateTicketDto
    ): ResponseEntity<OperationSuccessDto> {
        ticketService.createTicket(token.substring("Bearer ".length), createTicketDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    @PostMapping("{identifier}/assign")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun assignTicket(
        @RequestHeader("Authorization") token: String,
        @PathVariable("identifier") identifier: String
    ): ResponseEntity<OperationSuccessDto> {
        ticketService.assignTicket(token.substring("Bearer ".length), identifier)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    @PostMapping("{identifier}/update")
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun updateTicket(
        @PathVariable("identifier") identifier: String,
        @RequestBody updateTicketDto: UpdateTicketDto
    ): ResponseEntity<OperationSuccessDto> {
        ticketService.updateTicket(identifier, updateTicketDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }

    @GetMapping("/my")
    fun getMyTickets(
        @RequestHeader("Authorization") token: String,
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<TicketDto>> {
        return ResponseEntity.ok(ticketService.getMyTickets(token.substring("Bearer ".length), start, limit))
    }

    @GetMapping("/my/{identifier}")
    fun getMyTicket(
        @RequestHeader("Authorization") token: String,
        @PathVariable("identifier") identifier: String
    ): ResponseEntity<TicketDto> {
        return ResponseEntity.ok(ticketService.getMyTicket(token.substring("Bearer ".length), identifier))
    }
}