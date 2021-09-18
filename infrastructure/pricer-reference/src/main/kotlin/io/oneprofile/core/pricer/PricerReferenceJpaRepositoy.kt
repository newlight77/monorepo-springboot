package io.oneprofile.core.pricer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PricerReferenceJpaRepository : JpaRepository<PricerReferenceEntity, Long> {
    fun save(entity: PricerReferenceEntity): PricerReferenceEntity
    @Query("SELECT t FROM PricerReferenceEntity t ORDER by t.lastDate DESC")
    fun all(): List<PricerReferenceEntity>
}
