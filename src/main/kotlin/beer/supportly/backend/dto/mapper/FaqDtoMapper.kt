package beer.supportly.backend.dto.mapper

import beer.supportly.backend.database.entities.FaqEntity
import beer.supportly.backend.dto.FaqDto
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class FaqDtoMapper(
    private val userDtoMapper: UserDtoMapper
) : Function<FaqEntity, FaqDto> {

    override fun apply(faqEntity: FaqEntity): FaqDto {
        return FaqDto(
            faqEntity.id!!,
            faqEntity.title,
            faqEntity.text,
            faqEntity.createdAt,
            userDtoMapper.apply(faqEntity.creator)
        )
    }
}