package io.tricefal.core.security.exception

class ForbiddenException(val msg: String) : RuntimeException()
class UnauthorizedException(val msg: String) : RuntimeException()
