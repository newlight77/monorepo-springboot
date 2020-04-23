package io.tricefal.core.login

interface ILoginRepository<T, U> {
    fun save(login: T)
    fun findByUsername(username: String): List<T>
}