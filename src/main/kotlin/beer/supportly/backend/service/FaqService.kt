package beer.supportly.backend.service

import beer.supportly.backend.database.entities.FaqEntity
import beer.supportly.backend.database.repositories.FaqRepository
import beer.supportly.backend.dto.CreateFaqDto
import beer.supportly.backend.dto.FaqDto
import beer.supportly.backend.dto.mapper.FaqDtoMapper
import beer.supportly.backend.exception.BackendException
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.stream.Collectors

/**
 * Service for FAQ entries.
 *
 * @property faqRepository The FAQ repository.
 * @property faqDtoMapper The FAQ DTO mapper.
 * @property userService The user service.
 *
 * @see beer.supportly.backend.database.repositories.FaqRepository
 * @see beer.supportly.backend.dto.mapper.FaqDtoMapper
 * @see beer.supportly.backend.service.UserService
 */
@Service
class FaqService(
    private val faqRepository: FaqRepository,
    private val faqDtoMapper: FaqDtoMapper,
    private val userService: UserService
) {

    /**
     * Gets a FAQ entry.
     *
     * @param id The ID of the FAQ entry.
     *
     * @return The FAQ DTO.
     *
     * @throws BackendException If the FAQ entry is not found.
     *
     * @see beer.supportly.backend.dto.FaqDto
     */
    fun getFaqEntry(id: Long): FaqDto {
        return faqRepository.findById(id)
            .map(faqDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }
    }

    /**
     * Gets all FAQ entries.
     *
     * @param start The start index.
     * @param limit The limit.
     *
     * @return The list of FAQ DTOs.
     *
     * @see beer.supportly.backend.dto.FaqDto
     */
    fun getFaqEntries(start: Int, limit: Int): List<FaqDto> {
        return faqRepository.findAll(PageRequest.of(start, limit)).stream()
            .map(faqDtoMapper)
            .collect(Collectors.toList())
    }

    /**
     * Creates a FAQ entry.
     *
     * @param token The token.
     * @param createFaqDto The create FAQ DTO.
     *
     * @throws BackendException If the user is not found.
     *
     * @see beer.supportly.backend.dto.CreateFaqDto
     * @see beer.supportly.backend.database.entities.FaqEntity
     */
    fun createFaqEntry(token: String, createFaqDto: CreateFaqDto) {
        val creator = userService.getOriginalUserFromToken(token)

        val faqEntity = FaqEntity(
            createFaqDto.title,
            createFaqDto.text,
            System.currentTimeMillis(),
            creator
        )

        faqRepository.save(faqEntity)
    }
}