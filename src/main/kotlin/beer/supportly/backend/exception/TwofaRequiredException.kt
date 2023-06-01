package beer.supportly.backend.exception

/**
 * Exception thrown by the backend.
 *
 * @param message the message to return
 * @param twofaToken the 2FA token to return
 */
class TwofaRequiredException(message: String, val twofaToken: String) : RuntimeException(message)
