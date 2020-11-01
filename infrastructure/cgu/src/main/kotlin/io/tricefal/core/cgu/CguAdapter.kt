package io.tricefal.core.cgu

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
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
        return fromEntity(entity)
    }

    override fun findByUsername(username: String): Optional<CguDomain> {
        return repository.findByUsername(username).stream().findFirst().map {
            fromEntity(it)
        }
    }

    override fun acceptCgu(cgu: CguDomain): CguDomain {
        val mewEntity = toEntity(cgu)
        repository.findByUsername(cgu.username).stream().findFirst().ifPresentOrElse(
            {
                mewEntity.id = it.id
            },
            {
                logger.error("unable to find a cgu with username ${cgu.username}")
                throw CguNotFoundException("unable to find a cgu with username ${cgu.username}")
            }
        )

        val entity = repository.save(mewEntity)
        return fromEntity(entity)
    }

    class CguNotFoundException(private val msg: String) : Throwable(msg) {}
}





