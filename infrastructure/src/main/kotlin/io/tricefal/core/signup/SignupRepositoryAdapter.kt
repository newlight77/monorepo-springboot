package io.tricefal.core.signup

import io.tricefal.core.login.SignupJpaRepository
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.util.*

@Repository
class SignupRepositoryAdapter(private var repository: SignupJpaRepository) : ISignupRepository {
    override fun save(signup: SignupDomain) {
        println(signup)
        repository.save(toEntity(signup))
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        println("findByUsername$username")
        return repository.findByUsername(username).map {
            println("map : $username")
            fromEntity(it)
        }
    }

    override fun update(signup: SignupDomain) {
        repository.save(toEntity(signup))
    }

}





