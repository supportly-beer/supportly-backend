package beer.supportly.backend.dto

/**
 * Data transfer object for twofa.
 *
 * @property email The email of the user.
 * @property token The token of the user.
 *
 * @constructor constructor with all values
 */
data class TwofaDto(
    val email: String,
    val token: String
)
