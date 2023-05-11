package beer.supportly.backend.exception

import org.springframework.http.HttpStatus

class BackendException(val status: HttpStatus, message: String) : RuntimeException(message)