package io.oneprofile.core.security.ip

import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class IpAddressRepositoryAdapter(private var repository: IpAddressJpaRepository) {
    fun save(entity: IpAddressEntity) {
        entity.lastDate = Instant.now()
        repository.save(entity)
    }

    fun findByIpAddress(entity: String): List<IpAddressEntity> {
        return repository.findByIpAddress(entity)
    }

    fun findAll(): List<IpAddressEntity> {
        return repository.findAll()
    }

}





