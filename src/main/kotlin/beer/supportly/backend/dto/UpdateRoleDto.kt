package beer.supportly.backend.dto

/**
 * DTO for updating the role of a user.
 *
 * @property role The new role.
 *
 * @constructor constructor with all values
 */
data class UpdateRoleDto(
    val userId: Long,
    val role: String
)