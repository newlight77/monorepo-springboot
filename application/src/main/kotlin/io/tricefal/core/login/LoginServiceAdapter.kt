package io.tricefal.core.login

import org.springframework.stereotype.Service

@Service
class LoginServiceAdapter(val loginService: ILoginService<LoginDomain, Long>) : ILoginService<LoginModel, Long> {
    override fun create(login: LoginModel) {
        loginService.create(fromModel(login))
    }

    override fun findByUsername(username: String): List<LoginModel> {
        return loginService.findByUsername(username).map { toModel(it) }
    }

}