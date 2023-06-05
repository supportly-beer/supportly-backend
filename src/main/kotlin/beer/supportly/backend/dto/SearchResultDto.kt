package beer.supportly.backend.dto

/**
 * Data transfer object for search result.
 *
 * @property query The query that was used to search.
 * @property resultCount The number of results.
 * @property timeTook The time it took to search.
 * @property results The results.
 *
 * @constructor constructor with all values
 */
data class SearchResultDto(
    val query: String,
    val resultCount: Int,
    val timeTook: Int,
    val results: List<TicketSearchResultDto>
)