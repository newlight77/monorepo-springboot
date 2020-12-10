package io.tricefal.core.exception

class ConflictException(val msg: String) : RuntimeException()
class NotFoundException(val msg: String) : RuntimeException()
class NotAcceptedException(val msg: String) : RuntimeException()
