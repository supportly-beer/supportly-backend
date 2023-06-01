package beer.supportly.backend.dto

/**
 * Data transfer object for operation success.
 *
 * @property successful The success of the operation.
 * @property error The error message of the operation.
 *
 * @constructor constructor with all values
 */
data class OperationSuccessDto(
    val successful: Boolean,
    val error: String?
)