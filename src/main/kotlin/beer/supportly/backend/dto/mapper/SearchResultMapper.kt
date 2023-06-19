package beer.supportly.backend.dto.mapper

import beer.supportly.backend.dto.SearchResultDto
import beer.supportly.backend.dto.TicketSearchResultDto
import com.meilisearch.sdk.model.Searchable
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * Maps a Searchable to a SearchResultDto.
 *
 * @see beer.supportly.backend.dto.SearchResultDto
 */
@Component
class SearchResultMapper : Function<Searchable, SearchResultDto> {

    /**
     * Maps a Searchable to a SearchResultDto.
     *
     * @param searchable the Searchable to map
     *
     * @return SearchResultDto mapped from Searchable
     *
     * @see beer.supportly.backend.dto.SearchResultDto
     */
    override fun apply(searchable: Searchable): SearchResultDto {
        return SearchResultDto(
            searchable.query,
            searchable.hits.size,
            searchable.processingTimeMs,
            searchable.hits.map {
                TicketSearchResultDto(
                    (it["id"] as String).toLong(),
                    it["identifier"] as String,
                    it["title"] as String,
                    it["description"] as String
                )
            }
        )
    }
}