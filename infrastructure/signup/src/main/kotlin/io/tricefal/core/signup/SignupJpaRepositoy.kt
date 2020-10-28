package io.tricefal.core.login

import io.tricefal.core.signup.SignupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface SignupJpaRepository : JpaRepository<SignupEntity, Long> {
    fun save(entity: SignupEntity): SignupEntity
    @Query("SELECT t FROM SignupEntity t where t.username like %:username% ORDER by t.signupDate DESC")
    fun findByUsername(username: String): List<SignupEntity>
}
