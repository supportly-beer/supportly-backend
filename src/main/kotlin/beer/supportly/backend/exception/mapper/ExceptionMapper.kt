package beer.supportly.backend.exception.mapper

import beer.supportly.backend.dto.OperationSuccessDto
import beer.supportly.backend.exception.BackendException
import jakarta.servlet.http.HttpServletRequest
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
}