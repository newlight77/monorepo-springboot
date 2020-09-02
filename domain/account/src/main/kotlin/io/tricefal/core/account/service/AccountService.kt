package io.tricefal.core.account.service

import io.tricefal.core.account.api.IAccountRepository
import io.tricefal.core.account.api.IAccountService
import io.tricefal.core.account.domain.AccountDomain
import java.util.*


class AccountService(private var repository: IAccountRepository<AccountDomain, Long>) : IAccountService<AccountDomain, Long> {

    override fun create(account: AccountDomain) {
        repository.save(account)
    }

    override fun update(account: AccountDomain) {
        repository.update(account)
    }

    override fun delete(id: Long) {
        repository.delete(id)
    }

    override fun findAll(): List<AccountDomain> {
        return repository.findAll()
    }

    override fun findById(id: Long): Optional<AccountDomain> {
        return repository.findById(id)
    }

    override fun find(username: String, email: String): List<AccountDomain> {
        return repository.findByUsernameOrEmail(username, email)
    }

}