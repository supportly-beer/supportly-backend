package beer.supportly.backend.dto

/**
 * Data transfer object for token.
 *
 * @property message The message of the token.
 * @property token The token of the token.
 *
 * @constructor constructor with all values
 */
data class TokenDto(
    val message: String,
    val token: String
)
