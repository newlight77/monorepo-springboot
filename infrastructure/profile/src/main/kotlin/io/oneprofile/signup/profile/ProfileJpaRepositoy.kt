package io.oneprofile.signup.profile

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProfileJpaRepository : JpaRepository<ProfileEntity, Long> {
    fun save(entity: ProfileEntity): ProfileEntity
    @Query("SELECT t FROM ProfileEntity t where t.username like %:username% ORDER by t.lastDate DESC")
    fun findByUsername(username: String): List<ProfileEntity>
}
