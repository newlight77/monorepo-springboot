package io.tricefal.core.login

import io.tricefal.core.login.model.LoginModel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("login")
class LoginApi(val loginService: ILoginService<LoginModel, Long>) {

    @PostMapping("")
    fun create(@RequestBody login: LoginModel) {
        loginService.create(login)
    }

    @GetMapping("")
    fun find(@RequestParam username: String) {
        loginService.findByUsername(username)
    }
}