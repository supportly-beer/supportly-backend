package beer.supportly.backend.controller

import beer.supportly.backend.dto.CreateFaqDto
import beer.supportly.backend.dto.FaqDto
import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.service.FaqService
import jakarta.annotation.security.RolesAllowed
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller for the faq.
 *
 * @property faqService the service for the faq
 *
 * @see beer.supportly.backend.service.FaqService
 */
@RestController
@RequestMapping("/faq")
class FaqController(
    private val faqService: FaqService
) {

    /**
     * Endpoint for a faq entry.
     *
     * @param id contains the id of the faq entry
     *
     * @return FaqDto contains the faq entry
     *
     * @see beer.supportly.backend.dto.FaqDto
     */
    @GetMapping("/{id}")
    fun getFaqEntry(
        @PathVariable("id") id: Long
    ): ResponseEntity<FaqDto> {
        return ResponseEntity.ok(faqService.getFaqEntry(id))
    }

    /**
     * Endpoint for all faq entries.
     *
     * @param start contains the start
     * @param limit contains the limit
     *
     * @return List<FaqDto> contains the faq entries
     *
     * @see beer.supportly.backend.dto.FaqDto
     */
    @GetMapping
    fun getAllFaqEntries(
        @RequestParam(name = "start", required = true) start: Int,
        @RequestParam(name = "limit", required = true) limit: Int
    ): ResponseEntity<List<FaqDto>> {
        return ResponseEntity.ok(faqService.getFaqEntries(start, limit))
    }

    /**
     * Endpoint to create a faq entry.
     *
     * @param token contains the token
     * @param createFaqDto contains the data for the faq entry
     *
     * @return OperationSuccessDto contains the success
     *
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
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