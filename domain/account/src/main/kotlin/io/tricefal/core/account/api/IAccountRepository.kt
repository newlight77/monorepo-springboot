package io.tricefal.core.account.api

import java.util.*

interface IAccountRepository<T, U> {
    fun save(account: T)
    fun update(account: T)
    fun delete(id: U)
    fun findAll(): List<T>
    fun findById(id: U): Optional<T>
    fun findByUsernameOrEmail(username: String, email: String): List<T>
}