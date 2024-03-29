package io.oneprofile.signup.company

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CompanyJpaRepository : JpaRepository<CompanyEntity, Long> {
    fun save(entity: CompanyEntity): CompanyEntity
    @Query("SELECT t FROM CompanyEntity t where t.raisonSocial like %:companyName% ORDER by t.lastDate DESC")
    fun findByName(companyName: String): List<CompanyEntity>
}
