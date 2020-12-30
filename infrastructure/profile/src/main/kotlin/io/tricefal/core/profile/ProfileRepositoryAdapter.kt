package io.tricefal.core.profile

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProfileRepositoryAdapter(private var repository: ProfileJpaRepository) : ProfileDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(profile: ProfileDomain): ProfileDomain {
        val entity = toEntity(profile)
        return fromEntity(repository.save(entity))
    }

    override fun findByUsername(username: String): Optional<ProfileDomain> {
        return repository.findByUsername(username).stream().findFirst()
                .map {
            fromEntity(it)
        }
    }

    override fun update(profile: ProfileDomain): ProfileDomain {
        val mewEntity = toEntity(profile)
        repository.findByUsername(profile.username).stream().findFirst().ifPresentOrElse(
            {
                mewEntity.id = it.id
            },
            {
                logger.error("unable to find a profile with username ${profile.username}")
                throw ProfileNotFoundException("unable to find a profile with username ${profile.username}")
            }
        )

        repository.save(mewEntity)
        return fromEntity(mewEntity)
    }

    class ProfileNotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}





