package beer.supportly.backend.dto

/**
 * Data transfer object for forgot password.
 *
 * @property email The email of the user.
 *
 * @constructor constructor with all values
 */
data class ForgotPasswordDto(
    val email: String
)
