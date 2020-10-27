package io.tricefal.core.freelance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface FreelanceJpaRepository : JpaRepository<FreelanceEntity, Long> {
    fun save(entity: FreelanceEntity): FreelanceEntity
    @Query("SELECT t FROM FreelanceEntity t where t.username like %:username%")
    fun findByUsername(username: String): List<FreelanceEntity>
}
