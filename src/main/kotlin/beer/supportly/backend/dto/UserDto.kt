package beer.supportly.backend.dto

data class UserDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    val role: RoleDto
)