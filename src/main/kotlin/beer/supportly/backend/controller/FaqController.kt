package beer.supportly.backend.controller

import beer.supportly.backend.dto.CreateFaqDto
import beer.supportly.backend.dto.FaqDto
import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.service.FaqService
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/faq")
class FaqController(
    private val faqService: FaqService
) {

    @GetMapping("/{id}")
    fun getFaqEntry(
        @PathVariable("id") id: Long
    ): ResponseEntity<FaqDto> {
        return ResponseEntity.ok(faqService.getFaqEntry(id))
    }

    @GetMapping
    fun getAllFaqEntries(
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<FaqDto>> {
        return ResponseEntity.ok(faqService.getFaqEntries(start, limit))
    }

    @PostMapping
    @RolesAllowed("ROLE_ADMINISTRATOR", "ROLE_AGENT")
    fun createFaqEntry(
        @RequestHeader("Authorization") token: String,
        @RequestBody createFaqDto: CreateFaqDto
    ): ResponseEntity<OperationSuccessDto> {
        faqService.createFaqEntry(token.substring("Bearer ".length), createFaqDto)
        return ResponseEntity.ok(OperationSuccessDto(true, null))
    }
}