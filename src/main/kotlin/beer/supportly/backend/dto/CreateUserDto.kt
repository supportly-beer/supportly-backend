package beer.supportly.backend.dto

/**
 * Data transfer object for creating a user.
 *
 * @property email The email of the user.
 * @property firstName The first name of the user.
 * @property lastName The last name of the user.
 * @property password The password of the user.
 * @property profilePictureUrl The profile picture URL of the user.
 *
 * @constructor constructor with all values
 */
data class CreateUserDto(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val profilePictureUrl: String
)