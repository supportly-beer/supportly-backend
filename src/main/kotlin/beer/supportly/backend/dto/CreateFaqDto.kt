package beer.supportly.backend.dto

/**
 * Data transfer object for creating a faq.
 *
 * @property title The title of the faq.
 * @property text The text of the faq.
 *
 * @constructor constructor with all values
 */
data class CreateFaqDto(
    val title: String,
    val text: String
)
