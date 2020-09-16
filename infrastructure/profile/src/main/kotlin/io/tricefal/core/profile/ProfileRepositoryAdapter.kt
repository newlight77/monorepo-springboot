package io.tricefal.core.profile

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProfileRepositoryAdapter(private var repository: ProfileJpaRepository) : IProfileAdapter {
    override fun save(profile: ProfileDomain) {
        val entity = toEntity(profile)
        repository.save(entity)
    }

    override fun findByUsername(username: String): Optional<ProfileDomain> {
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }
}





