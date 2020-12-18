package io.tricefal.core.freelance

import io.tricefal.shared.util.json.JsonPatchOperator
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FreelanceRepositoryAdapter(private var repository: FreelanceJpaRepository) : FreelanceDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        repository.findByUsername(freelance.username).stream().findFirst().ifPresent {
            logger.error("a freelance with username ${freelance.username} is already taken")
            throw DuplicateKeyException("a freelance with username ${freelance.username} is already taken")
        }
        val freelanceEntity = repository.save(toEntity(freelance))
        return fromEntity(freelanceEntity)
    }

    override fun findAll(): List<FreelanceDomain> {
        return repository.findAll().map {
            fromEntity(it)
        }
    }

    override fun availables(): List<FreelanceDomain> {
        return repository.findByStatus(Status.AVAILABLE.toString()) // may return AVAILABLE_SOON
                .filter { Status.AVAILABLE.toString() == it.status?.toUpperCase() }
                .map { fromEntity(it) }
    }

    override fun findByUsername(username: String): Optional<FreelanceDomain> {
        return repository.findByUsername(username).stream().findFirst().map {
            fromEntity(it)
        }
    }

    override fun update(freelance: FreelanceDomain): Optional<FreelanceDomain> {
        val entity = repository.findByUsername(freelance.username).stream().findFirst()
        var updated = Optional.empty<FreelanceDomain>()
        entity.ifPresentOrElse(
            {
                val mewEntity = toEntity(freelance)
                mewEntity.id = it.id
                mewEntity.company?.id = it.company?.id
                mewEntity.contact?.id = it.contact?.id
                mewEntity.privacyDetail?.id = it.privacyDetail?.id
                mewEntity.state?.id = it.state?.id
                repository.save(mewEntity)
                updated =  Optional.of(fromEntity(mewEntity))
            },
            {
                logger.error("unable to find a freelance with username ${freelance.username}")
                throw FreelanceNotFoundException("unable to find a freelance with username ${freelance.username}")
            }
        )
        return updated
    }

    // TODO : use event publisher and listener for persistence
    override fun patch(freelance: FreelanceDomain, operations: List<PatchOperation>): Optional<FreelanceDomain> {
        val entity = repository.findByUsername(freelance.username).stream().findFirst()
        var updated = Optional.empty<FreelanceDomain>()
        entity.ifPresentOrElse(
            {
                operations.let { ops ->
                    val patched = JsonPatchOperator().apply(entity.get(), ops)
                    repository.save(patched)
                    updated =  Optional.of(fromEntity(patched))
                }
            },
            {
                logger.error("unable to find a freelance with username ${freelance.username}")
                throw FreelanceNotFoundException("unable to find a freelance with username ${freelance.username}")
            }
        )
        return updated
    }

    class FreelanceNotFoundException(private val msg: String) : Throwable(msg) {}
}





