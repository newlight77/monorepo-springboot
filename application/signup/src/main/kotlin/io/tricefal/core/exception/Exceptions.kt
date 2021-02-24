package io.tricefal.core.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class GlobalConflictException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class GlobalNotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class GlobalNotAcceptedException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class GlobalBadRequestException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}