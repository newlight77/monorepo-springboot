package io.tricefal.core.login

interface ILoginRepository {
    fun save(login: LoginDomain)
    fun findByUsername(username: String): List<LoginDomain>
}