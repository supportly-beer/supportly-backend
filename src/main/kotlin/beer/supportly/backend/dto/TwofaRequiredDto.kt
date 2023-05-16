package beer.supportly.backend.dto

data class TwofaRequiredDto(
    val message: String,
    val token: String
)