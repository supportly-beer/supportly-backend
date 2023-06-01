package beer.supportly.backend.exception.mapper

import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.dto.TokenDto
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.exception.TwofaRequiredException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * This class is responsible for mapping exceptions to HTTP responses.
 *
 * @see beer.supportly.backend.exception.BackendException
 * @see beer.supportly.backend.exception.TwofaRequiredException
 */
@ControllerAdvice
class ExceptionMapper {

    /**
     * This method maps BackendException to HTTP response.
     *
     * @param backendException exception to be mapped
     * @param httpServletRequest request that caused the exception
     *
     * @return OperationSuccessDto response with status code and message
     *
     * @see beer.supportly.backend.exception.BackendException
     * @see beer.supportly.backend.dto.OperationSuccessDto
     */
    @ExceptionHandler(BackendException::class)
    fun handleBackendException(
        backendException: BackendException,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<OperationSuccessDto> {
        return ResponseEntity.status(backendException.status).body(OperationSuccessDto(false, backendException.message))
    }

    /**
     * This method maps TwofaRequiredException to HTTP response.
     *
     * @param twoFaRequiredException exception to be mapped
     * @param httpServletRequest request that caused the exception
     *
     * @return TokenDto response with status code, message and twofa token
     *
     * @see beer.supportly.backend.exception.TwofaRequiredException
     * @see beer.supportly.backend.dto.TokenDto
     */
    @ExceptionHandler(TwofaRequiredException::class)
    fun handleTwofaRequiredException(
        twoFaRequiredException: TwofaRequiredException,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<TokenDto> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(TokenDto(twoFaRequiredException.message.orEmpty(), twoFaRequiredException.twofaToken))
    }
}