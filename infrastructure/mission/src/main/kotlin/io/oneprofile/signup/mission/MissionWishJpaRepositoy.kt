package io.oneprofile.signup.mission

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MissionWishJpaRepository : JpaRepository<MissionWishEntity, Long> {
    fun save(entity: MissionWishEntity): MissionWishEntity
    @Query("SELECT t FROM MissionWishEntity t where t.username like %:username% ORDER by t.lastDate DESC")
    fun findByUsername(username: String): List<MissionWishEntity>
}
