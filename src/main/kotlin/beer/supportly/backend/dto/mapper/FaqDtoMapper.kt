package beer.supportly.backend.dto.mapper

import beer.supportly.backend.database.entities.FaqEntity
import beer.supportly.backend.dto.FaqDto
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * Maps a FaqEntity to a FaqDto.
 *
 * @see beer.supportly.backend.database.entities.FaqEntity
 * @see beer.supportly.backend.dto.FaqDto
 * @see beer.supportly.backend.dto.mapper.UserDtoMapper
 */
@Component
class FaqDtoMapper(
    private val userDtoMapper: UserDtoMapper
) : Function<FaqEntity, FaqDto> {

    /**
     * Maps a FaqEntity to a FaqDto.
     *
     * @param faqEntity the FaqEntity to map
     *
     * @return FaqDto mapped from FaqEntity
     *
     * @see beer.supportly.backend.database.entities.FaqEntity
     * @see beer.supportly.backend.dto.FaqDto
     * @see beer.supportly.backend.dto.mapper.UserDtoMapper
     */
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