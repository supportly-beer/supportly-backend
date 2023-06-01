package beer.supportly.backend.dto

/**
 * Data transfer object for twofa enabled.
 *
 * @property qrCode The qrCode of the twofa enabled.
 *
 * @constructor constructor with all values
 */
data class TwofaEnabledDto(
    val qrCode: String
)
