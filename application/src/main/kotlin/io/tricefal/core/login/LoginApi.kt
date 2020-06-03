package io.tricefal.core.login

import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("logins")
class LoginApi(val loginHandler: LoginWebHandler) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun login(@RequestBody login: LoginModel) {
        print("saving login : $login")
        loginHandler.login(login)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun list() : List<LoginModel>{
        val username: String =  SecurityContextHolder.getContext().getAuthentication().name
        return loginHandler.findByUsername(username)
    }
}