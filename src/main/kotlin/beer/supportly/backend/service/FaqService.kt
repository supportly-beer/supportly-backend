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

@Service
class FaqService(
    private val faqRepository: FaqRepository,
    private val faqDtoMapper: FaqDtoMapper,
    private val userService: UserService
) {
    fun getFaqEntry(id: Long): FaqDto {
        return faqRepository.findById(id)
            .map(faqDtoMapper)
            .orElseThrow { BackendException(HttpStatus.NOT_FOUND, "Ticket not found") }
    }

    fun getFaqEntries(start: Int, limit: Int): List<FaqDto> {
        return faqRepository.findAll(PageRequest.of(start, limit)).stream()
            .map(faqDtoMapper)
            .collect(Collectors.toList())
    }

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