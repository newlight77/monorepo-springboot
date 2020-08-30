package io.tricefal.core.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class HomeApi() {

    @GetMapping("hello")
    @ResponseStatus(HttpStatus.OK)
    fun hello(): String {
        return "hello"
    }
}