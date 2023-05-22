package beer.supportly.backend.exception

class TwofaRequiredException(message: String, val twofaToken: String) : RuntimeException(message)
