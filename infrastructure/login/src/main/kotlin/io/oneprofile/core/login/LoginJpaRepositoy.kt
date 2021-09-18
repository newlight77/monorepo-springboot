package io.oneprofile.core.login

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LoginJpaRepository : JpaRepository<LoginEntity, Long> {
    fun save(entity: LoginEntity)
    @Query("SELECT t FROM LoginEntity t where t.username like %:username% ORDER by t.loginDate DESC")
    fun findByUsername(username: String): List<LoginEntity>
}
