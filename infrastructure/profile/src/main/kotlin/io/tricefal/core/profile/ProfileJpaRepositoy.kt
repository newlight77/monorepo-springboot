package io.tricefal.core.profile

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ProfileJpaRepository : JpaRepository<ProfileEntity, Long> {
    fun save(entity: ProfileEntity)
    @Query("SELECT t FROM ProfileEntity t where t.username like %:username%")
    fun findByUsername(username: String): Optional<ProfileEntity>
}
