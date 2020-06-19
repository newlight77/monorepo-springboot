package io.tricefal.core.login

import org.springframework.stereotype.Service

@Service
class LoginWebHandler(val loginService: ILoginService) {
    fun login(login: LoginModel) {
        loginService.create(fromModel(login))
    }

    fun findByUsername(username: String): List<LoginModel> {
        return loginService.findByUsername(username).map { toModel(it) }
    }
}