package io.tricefal.core.account.api

import java.util.*

interface IAccountService<T, U> {
    fun create(account: T)
    fun update(account: T)
    fun delete(id: U)
    fun findAll(): List<T>
    fun findById(id: U): Optional<T>
    fun find(username: String, email: String): List<T>
}