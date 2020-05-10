package io.tricefal.core.login

import org.springframework.stereotype.Repository

@Repository
class LoginRepositoryAdapter(private var repository: LoginJpaRepository) : ILoginRepository {
    override fun save(login: LoginDomain) {
        val entity = toEntity(login)
        repository.save(entity)
    }

    override fun findByUsername(username: String): List<LoginDomain> {
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }
}





