package io.oneprofile.core.security.ip

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IpAddressJpaRepository : JpaRepository<IpAddressEntity, Long> {
    fun save(entity: IpAddressEntity)
    @Query("SELECT t FROM IpAddressEntity t where t.ipAddress like %:ipAddress% ORDER by t.lastDate DESC")
    fun findByIpAddress(ipAddress: String): List<IpAddressEntity>
}
