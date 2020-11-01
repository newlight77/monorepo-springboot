package io.tricefal.core.cgu

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CguJpaRepository : JpaRepository<CguEntity, Long> {
    fun save(entity: CguEntity): CguEntity
    @Query("SELECT t FROM CguEntity t where t.username like %:username% ORDER by t.lastDate DESC")
    fun findByUsername(username: String): List<CguEntity>
}
