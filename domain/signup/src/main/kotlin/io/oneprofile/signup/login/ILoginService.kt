package io.oneprofile.signup.login

interface ILoginService {
    fun create(login: LoginDomain)
    fun findByUsername(username: String): List<LoginDomain>
}