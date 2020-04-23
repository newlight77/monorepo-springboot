package io.tricefal.core.login

interface ILoginService<T, U> {
    fun create(login: T)
    fun findByUsername(username: String): List<T>
}