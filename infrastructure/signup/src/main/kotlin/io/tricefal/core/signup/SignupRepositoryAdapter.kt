package io.tricefal.core.signup

import io.tricefal.core.login.SignupJpaRepository
import org.springframework.stereotype.Repository
import java.io.InputStream
import java.util.*

@Repository
class SignupRepositoryAdapter(private var repository: SignupJpaRepository) : ISignupRepository {
    override fun save(signup: SignupDomain): SignupDomain {
        println(signup)
        val signupEntity = repository.save(toEntity(signup))
        return fromEntity(signupEntity)
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        println("findByUsername$username")
        return repository.findByUsername(username).map {
            println("map : $username")
            fromEntity(it)
        }
    }

    override fun update(signup: SignupDomain): SignupDomain {
        val signupEntity = repository.save(toEntity(signup))
        return fromEntity(signupEntity)
    }

}





