package io.tricefal.core.security.ip

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class IpAddressRepositoryAdapter(private var repository: IpAddressJpaRepository) {
    fun save(ipAddress: IpAddressEntity) {
        repository.save(ipAddress)
    }

    fun findByIpAddress(ipAddress: String): List<IpAddressEntity> {
        return repository.findByIpAddress(ipAddress)
    }

    fun findAll(): List<IpAddressEntity> {
        return repository.findAll()
    }

}





