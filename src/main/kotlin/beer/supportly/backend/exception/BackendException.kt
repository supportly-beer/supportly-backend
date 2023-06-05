package beer.supportly.backend.exception

import org.springframework.http.HttpStatus

/**
 * Exception thrown by the backend.
 *
 * @property status the HTTP status code to return
 * @property message the message to return
 */
class BackendException(val status: HttpStatus, message: String) : RuntimeException(message)