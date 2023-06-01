package beer.supportly.backend.dto

/**
 * Data transfer object for login.
 *
 * @property email The email of the user.
 * @property password The password of the user.
 *
 * @constructor constructor with all values
 */
data class LoginDto(
    val email: String,
    val password: String
)
