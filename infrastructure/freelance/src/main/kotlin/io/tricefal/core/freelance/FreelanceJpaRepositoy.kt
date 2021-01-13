package io.tricefal.core.freelance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FreelanceJpaRepository : JpaRepository<FreelanceEntity, Long> {
    fun save(entity: FreelanceEntity): FreelanceEntity
    @Query("SELECT t FROM FreelanceEntity t where t.username like %:username% ORDER by t.lastDate DESC")
    fun findByUsername(username: String): List<FreelanceEntity>
    @Query("SELECT t FROM FreelanceEntity t where t.availability like %:status% ORDER by t.lastDate DESC")
    fun findByAvailability(status: String): List<FreelanceEntity>
}
