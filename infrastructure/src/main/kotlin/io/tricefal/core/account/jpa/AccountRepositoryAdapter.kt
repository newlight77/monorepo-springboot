package io.tricefal.core.account.jpa

import io.tricefal.core.account.api.IAccountRepository
import io.tricefal.core.account.domain.AccountDomain
import io.tricefal.core.account.entity.fromDomain
import io.tricefal.core.account.entity.toDomain
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepositoryAdapter(private var repository: AccountJpaRepository) : IAccountRepository<AccountDomain, Long> {
    override fun save(account: AccountDomain) {
        println(account)
        val d = fromDomain(account)
        repository.save(d)
    }

    override fun delete(id: Long) {
        println(id)
        val entity = repository.findById(id)
        if (entity.isPresent())
            repository.delete(entity.get())
    }

    override fun update(account: AccountDomain) {
        println(account)
        repository.save(fromDomain(account))
    }

    override fun findAll(): List<AccountDomain> {
        return repository.findAll().map {
            println("domain : $it")
            toDomain(it)
        }
    }

    override fun findById(id: Long): Optional<AccountDomain> {
        return repository.findById(id).map {
            println("domain : $it")
            toDomain(it)
        }
    }

    override fun findByUsernameOrEmail(username: String, email: String): List<AccountDomain> {
        println("findByUsername$username")
        return repository.findByUsername(username).union(repository.findByEmail(email))
                .map {
                    println("map : $username")
                    toDomain(it)
        }
    }
}





