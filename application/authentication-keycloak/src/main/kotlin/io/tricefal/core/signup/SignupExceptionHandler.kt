package io.tricefal.core.signup

import io.tricefal.core.exception.ExceptionDetail
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.Instant
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class SignupExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
            SignupUserNotFoundException::class,
            SignupResumeUploadException::class,
            SignupLinkedinResumeUploadException::class,
            SignupVerificationByCodeException::class,
            SignupStatusUpdateException::class,
            SignupPersistenceException::class,
            SignupUserRegistrationException::class,
            SignupRoleAssignationException::class,
            SignupCguAcceptException::class,
            SignupEmailNotificationException::class,
            SignupSmsNotificationException::class
    )

    fun handle400Exception(request: HttpServletRequest, ex: Exception): Any {
        return ExceptionDetail.Builder()
                .classname(ex.javaClass.name)
                .date(Instant.now().toString())
                .message(ex.localizedMessage)
                .path(request.requestURI)
                .params(request.queryString)
                .build()
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserDetailNotDefinedException::class)
    @ResponseBody
    fun handle401Exception(request: HttpServletRequest, ex: java.lang.Exception): Any {
        return ExceptionDetail.Builder()
                .classname(ex.javaClass.name)
                .date(Instant.now().toString())
                .message(ex.localizedMessage)
                .path(request.requestURI)
                .params(request.queryString)
                .build()
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SignupNotFoundException::class)
    @ResponseBody
    fun handle404Exception(request: HttpServletRequest, ex: java.lang.Exception): Any {
        return ExceptionDetail.Builder()
                .classname(ex.javaClass.name)
                .date(Instant.now().toString())
                .message(ex.localizedMessage)
                .path(request.requestURI)
                .params(request.queryString)
                .build()
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(
            SignupUsernameUniquenessException::class)
    fun handle409Exception(request: HttpServletRequest, ex: java.lang.Exception): Any {
        return ExceptionDetail.Builder()
                .classname(ex.javaClass.name)
                .date(Instant.now().toString())
                .message(ex.localizedMessage)
                .path(request.requestURI)
                .params(request.queryString)
                .build()
    }

}
