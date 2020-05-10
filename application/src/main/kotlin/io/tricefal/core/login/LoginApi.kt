package io.tricefal.core.login

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("logins")
class LoginApi(val loginHandler: LoginWebHandler) {

    @PostMapping("")
    fun login(@RequestBody login: LoginModel) {
        loginHandler.login(login)
    }

    @GetMapping("")
    fun list() : List<LoginModel>{
        val username: String =  SecurityContextHolder.getContext().getAuthentication().name
        return loginHandler.findByUsername(username)
    }
}