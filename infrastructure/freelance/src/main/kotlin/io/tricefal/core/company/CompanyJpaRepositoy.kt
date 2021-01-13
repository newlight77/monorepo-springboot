package io.tricefal.core.company

import io.tricefal.core.freelance.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CompanyJpaRepository : JpaRepository<CompanyEntity, Long> {
    fun save(entity: CompanyEntity): CompanyEntity
    @Query("SELECT t FROM CompanyEntity t where t.nomCommercial like %:companyName% ORDER by t.lastDate DESC")
    fun findByName(companyName: String): List<CompanyEntity>
}
