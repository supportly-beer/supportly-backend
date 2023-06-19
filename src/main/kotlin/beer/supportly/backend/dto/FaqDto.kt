package beer.supportly.backend.dto

/**
 * Data transfer object for a faq.
 *
 * @property id The id of the faq.
 * @property title The title of the faq.
 * @property text The text of the faq.
 * @property createdAt The creation date of the faq.
 * @property creator The creator of the faq.
 *
 * @constructor constructor with all values
 *
 * @see beer.supportly.backend.dto.UserDto
 */
data class FaqDto(
    val id: Long,
    val title: String,
    val text: String,
    val createdAt: Long,
    val creator: UserDto
)
