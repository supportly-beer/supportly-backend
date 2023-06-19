package beer.supportly.backend.dto

data class FaqDto(
    val id: Long,
    val title: String,
    val text: String,
    val createdAt: Long,
    val creator: UserDto
)
