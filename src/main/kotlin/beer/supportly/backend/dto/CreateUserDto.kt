package beer.supportly.backend.dto

data class CreateUserDto(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val profilePictureUrl: String
)