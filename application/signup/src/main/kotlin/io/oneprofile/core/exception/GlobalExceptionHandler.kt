package io.oneprofile.core.exception

import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.ResourceAccessException
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import kotlin.IllegalArgumentException


@ControllerAdvice
@Order(2)
class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        HttpMessageNotReadableException::class,
        MethodArgumentNotValidException::class,
        MissingServletRequestParameterException::class,
        IllegalArgumentException::class,
        java.lang.IllegalArgumentException::class,
        GlobalNotAcceptedException::class,
        GlobalBadRequestException::class)
    fun handle400Exception(request: HttpServletRequest, ex: Exception): Any {
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
    @ExceptionHandler(GlobalConflictException::class)
    fun handle409Exception(request: HttpServletRequest, ex: java.lang.Exception): Any {
        return ExceptionDetail.Builder()
                .classname(ex.javaClass.name)
                .date(Instant.now().toString())
                .message(ex.localizedMessage)
                .path(request.requestURI)
                .params(request.queryString)
                .build()
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
        ResourceAccessException::class,
        GlobalNotFoundException::class)
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(
            NullPointerException::class,
            java.lang.NullPointerException::class
    )
    fun handle500Exception(request: HttpServletRequest, ex: java.lang.Exception): Any {
        return ExceptionDetail.Builder()
                .classname(ex.javaClass.name)
                .date(Instant.now().toString())
                .message(ex.localizedMessage)
                .path(request.requestURI)
                .params(request.queryString)
                .build()
    }
}