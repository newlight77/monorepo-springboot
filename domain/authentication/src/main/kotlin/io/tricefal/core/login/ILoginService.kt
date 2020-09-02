package io.tricefal.core.login

interface ILoginService {
    fun create(login: LoginDomain)
    fun findByUsername(username: String): List<LoginDomain>
}