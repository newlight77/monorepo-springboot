package io.tricefal.core.login

import io.tricefal.core.login.domain.LoginDomain

class LoginService(private var repository: ILoginRepository<LoginDomain, Long>) : ILoginService<LoginDomain, Long> {
    override fun create(login: LoginDomain) {
        repository.save(login)
    }

    override fun findByUsername(username: String): List<LoginDomain> {
        return repository.findByUsername(username)
    }
}