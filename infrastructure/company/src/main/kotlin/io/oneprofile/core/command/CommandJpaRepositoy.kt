package io.oneprofile.core.command

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommandJpaRepository : JpaRepository<CommandEntity, Long> {
    fun save(entity: CommandEntity): CommandEntity
    @Query("SELECT t FROM CommandEntity t where t.companyName like %:companyName% ORDER by t.lastDate DESC")
    fun findByName(companyName: String): List<CommandEntity>
}
