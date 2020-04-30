package io.tricefal.core.login

import org.springframework.stereotype.Repository

@Repository
class LoginRepositoryAdapter(private var repository: LoginJpaRepository) : ILoginRepository<LoginDomain, Long> {
    override fun save(login: LoginDomain) {
        println(login)
        val entity = toEntity(login)
        repository.save(entity)
    }

    override fun findByUsername(username: String): List<LoginDomain> {
        println("findByUsername$username")
        return repository.findByUsername(username).map {
            println("map : $username")
            fromEntity(it)
        }
    }
}





