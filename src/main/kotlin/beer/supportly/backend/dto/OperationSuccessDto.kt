package beer.supportly.backend.dto

data class OperationSuccessDto(
    val successful: Boolean,
    val error: String?
)