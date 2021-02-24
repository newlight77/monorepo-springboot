package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class MissionWishRepositoryAdapter(private var repository: MissionWishJpaRepository,
                                   private var publisher: MissionWishEventPublisher) : MissionWishDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(missionWish: MissionWishDomain): MissionWishDomain {
        repository.findByUsername(missionWish.username).stream().findFirst().ifPresent {
            logger.error("a missionWish with username ${missionWish.username} is already taken")
            throw DuplicateKeyException("a missionWish with username ${missionWish.username} is already taken")
        }
        val entity = repository.save(toEntity(missionWish))
        entity.lastDate = missionWish.lastDate ?: Instant.now()
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
        var newEntity = toEntity(missionWish)
        repository.findByUsername(missionWish.username).stream().findFirst().ifPresentOrElse(
            {
                newEntity.id = it.id
                newEntity.lastDate = Instant.now()
                newEntity = repository.save(newEntity)
            },
            {
                logger.error("unable to find a missionWish with username ${missionWish.username}")
                throw MissionWishNotFoundException("unable to find a missionWish with username ${missionWish.username}")
            }
        )

        return fromEntity(newEntity)
    }

    override fun updateOnResumeUploaded(username: String, filename: String) {
        publisher.publishMissionResumeUploadedEvent(username, filename)
    }

    class MissionWishNotFoundException(private val msg: String) : Throwable(msg) {}
}





