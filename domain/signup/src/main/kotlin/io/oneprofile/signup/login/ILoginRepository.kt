package io.oneprofile.signup.login

interface ILoginRepository {
    fun save(login: LoginDomain)
    fun findByUsername(username: String): List<LoginDomain>
}