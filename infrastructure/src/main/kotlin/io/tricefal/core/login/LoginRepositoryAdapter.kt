package io.tricefal.core.login

import io.tricefal.core.login.domain.LoginDomain
import io.tricefal.core.login.entity.fromDomain
import io.tricefal.core.login.entity.toDomain
import org.springframework.stereotype.Repository

@Repository
class LoginRepositoryAdapter(private var repository: LoginJpaRepository) : ILoginRepository<LoginDomain, Long> {
    override fun save(login: LoginDomain) {
        println(login)
        val entity = fromDomain(login)
        repository.save(entity)
    }

    override fun findByUsername(username: String): List<LoginDomain> {
        println("findByUsername$username")
        return repository.findByUsername(username).map {
            println("map : $username")
            toDomain(it)
        }
    }
}





