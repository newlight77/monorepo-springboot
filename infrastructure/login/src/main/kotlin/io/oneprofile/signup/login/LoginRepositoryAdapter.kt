package io.oneprofile.signup.login

import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class LoginRepositoryAdapter(private var repository: LoginJpaRepository) : ILoginRepository {
    override fun save(login: LoginDomain) {
        val entity = toEntity(login)
        entity.loginDate = login.loginDate ?: Instant.now()
        repository.save(entity)
    }

    override fun findByUsername(username: String): List<LoginDomain> {
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }
}





