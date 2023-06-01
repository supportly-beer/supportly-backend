package beer.supportly.backend.exception

import org.springframework.http.HttpStatus

/**
 * Exception thrown by the backend.
 *
 * @param status the HTTP status code to return
 * @param message the message to return
 */
class BackendException(val status: HttpStatus, message: String) : RuntimeException(message)