package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MissionWishAdapter(private var repository: MissionWishJpaRepository) : IMissionWishAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(missionWish: MissionWishDomain): MissionWishDomain {
        repository.findByUsername(missionWish.username).stream().findFirst().ifPresent {
            logger.error("a missionWish with username ${missionWish.username} is already taken")
            throw DuplicateKeyException("a missionWish with username ${missionWish.username} is already taken")
        }
        val entity = repository.save(toEntity(missionWish))
        return fromEntity(entity)
    }

    override fun findAll(): List<MissionWishDomain> {
        return repository.findAll().map {
            fromEntity(it)
        }
    }

    override fun findByUsername(username: String): Optional<MissionWishDomain> {
        return repository.findByUsername(username).stream().findFirst().map {
            fromEntity(it)
        }
    }

    override fun update(missionWish: MissionWishDomain): MissionWishDomain {
        val mewEntity = toEntity(missionWish)
        repository.findByUsername(missionWish.username).stream().findFirst().ifPresentOrElse(
            {
                mewEntity.id = it.id
            },
            {
                logger.error("unable to find a missionWish with username ${missionWish.username}")
                throw MissionWishNotFoundException("unable to find a missionWish with username ${missionWish.username}")
            }
        )

        val entity = repository.save(mewEntity)
        return fromEntity(entity)
    }

    class MissionWishNotFoundException(private val msg: String) : Throwable(msg) {}
}





