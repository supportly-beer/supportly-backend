package beer.supportly.backend.exception

/**
 * Exception thrown by the backend.
 *
 * @property message the message to return
 * @property twofaToken the 2FA token to return
 */
class TwofaRequiredException(message: String, val twofaToken: String) : RuntimeException(message)
