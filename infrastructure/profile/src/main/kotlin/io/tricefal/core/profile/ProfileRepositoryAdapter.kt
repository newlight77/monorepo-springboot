package io.tricefal.core.profile

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProfileRepositoryAdapter(private var repository: ProfileJpaRepository) : IProfileAdapter {
    override fun save(profile: ProfileDomain): ProfileDomain {
        val entity = toEntity(profile)
        return fromEntity(repository.save(entity))
    }

    override fun findByUsername(username: String): Optional<ProfileDomain> {
        return repository.findByUsername(username).stream().findFirst()
                .map {
            fromEntity(it)
        }
    }
}





