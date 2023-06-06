package beer.supportly.backend.dto

/**
 * Data transfer object for a user.
 *
 * @property id The id of the user.
 * @property email The email of the user.
 * @property firstName The first name of the user.
 * @property lastName The last name of the user.
 * @property profilePictureUrl The profile picture url of the user.
 * @property twofaEnabled The twofa enabled of the user.
 * @property emailVerified The email verified of the user.
 * @property role The role of the user.
 *
 * @constructor constructor with all values
 *
 * @see beer.supportly.backend.dto.RoleDto
 */
data class UserDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    val twofaEnabled: Boolean,
    val emailVerified: Boolean,
    val role: RoleDto
)