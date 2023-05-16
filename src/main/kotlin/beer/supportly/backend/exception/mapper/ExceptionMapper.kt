package beer.supportly.backend.exception.mapper

import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.dto.TwofaRequiredDto
import beer.supportly.backend.exception.BackendException
import beer.supportly.backend.exception.TwofaRequiredException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionMapper {

    @ExceptionHandler(BackendException::class)
    fun handleBackendException(
        backendException: BackendException,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<OperationSuccessDto> {
        return ResponseEntity.status(backendException.status).body(OperationSuccessDto(false, backendException.message))
    }

    @ExceptionHandler(TwofaRequiredException::class)
    fun handleTwofaRequiredException(
        twoFaRequiredException: TwofaRequiredException,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<TwofaRequiredDto> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(TwofaRequiredDto(twoFaRequiredException.message.orEmpty(), twoFaRequiredException.token))
    }
}