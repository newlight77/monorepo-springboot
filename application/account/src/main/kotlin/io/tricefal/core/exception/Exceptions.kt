package io.tricefal.core.exception

class ForbiddenException(val msg: String) : RuntimeException()
class ConflictException(val msg: String) : RuntimeException()
class NotFoundException(val msg: String) : RuntimeException()
class UnauthorizedException(val msg: String) : RuntimeException()
class NotAcceptedException(val msg: String) : RuntimeException()
