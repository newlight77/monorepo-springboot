package io.oneprofile.core.cgu

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class CguAdapter(private var repository: CguJpaRepository) : ICguAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(cgu: CguDomain): CguDomain {
        repository.findByUsername(cgu.username).stream().findFirst().ifPresent {
            logger.error("a cgu with username ${cgu.username} is already taken")
            throw DuplicateKeyException("a cgu with username ${cgu.username} is already taken")
        }
        val entity = repository.save(toEntity(cgu))
        entity.lastDate = cgu.lastDate ?: Instant.now()
        return fromEntity(entity)
    }

    override fun findByUsername(username: String): Optional<CguDomain> {
        return repository.findByUsername(username).stream().findFirst().map {
            fromEntity(it)
        }
    }

    override fun acceptCgu(cgu: CguDomain): CguDomain {
        var newEntity = toEntity(cgu)
        repository.findByUsername(cgu.username).stream().findFirst().ifPresentOrElse(
            {
                newEntity.id = it.id
                newEntity.lastDate = it.lastDate ?: Instant.now()
                newEntity = repository.save(newEntity)
            },
            {
                logger.error("unable to find a cgu with username ${cgu.username}")
                throw CguNotFoundException("unable to find a cgu with username ${cgu.username}")
            }
        )

        return fromEntity(newEntity)
    }

    class CguNotFoundException(private val msg: String) : Throwable(msg) {}
}





