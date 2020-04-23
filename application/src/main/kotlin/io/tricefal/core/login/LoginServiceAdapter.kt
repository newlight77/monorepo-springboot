package io.tricefal.core.login

import io.tricefal.core.login.domain.LoginDomain
import io.tricefal.core.login.model.LoginModel
import io.tricefal.core.login.model.fromDomain
import org.springframework.stereotype.Service

@Service
class LoginServiceAdapter(val loginService: ILoginService<LoginDomain, Long>) : ILoginService<LoginModel, Long> {
    override fun create(login: LoginModel) {
        loginService.create(io.tricefal.core.login.model.toDomain(login))
    }

    override fun findByUsername(username: String): List<LoginModel> {
        return loginService.findByUsername(username).map { fromDomain(it) }
    }

}