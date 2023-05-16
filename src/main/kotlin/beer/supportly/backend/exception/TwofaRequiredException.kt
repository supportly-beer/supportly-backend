package beer.supportly.backend.exception

class TwofaRequiredException(message: String, val token: String) : RuntimeException(message)
