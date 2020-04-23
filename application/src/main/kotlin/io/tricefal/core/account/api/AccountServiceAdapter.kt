package io.tricefal.core.account.api

import io.tricefal.core.account.domain.AccountDomain
import io.tricefal.core.account.model.AccountModel
import io.tricefal.core.account.model.fromDomain
import io.tricefal.core.account.model.toDomain
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountServiceAdapter(val accounService: IAccountService<AccountDomain, Long>) : IAccountService<AccountModel, Long> {
    override fun create(model: AccountModel) {
        accounService.create(toDomain(model))
    }

    override fun update(domain: AccountModel) {
        accounService.update(toDomain(domain))
    }

    override fun delete(id: Long) {
        accounService.delete(id)
    }

    override fun findAll(): List<AccountModel> {
        return accounService.findAll().map { fromDomain(it) }
    }

    override fun findById(id: Long): Optional<AccountModel> {
        return accounService.findById(id).map { fromDomain(it) }
    }

    override fun find(username: String, email: String): List<AccountModel> {
        return accounService.find(username, email).map { fromDomain(it) }
    }
}