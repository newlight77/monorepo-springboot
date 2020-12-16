package io.tricefal.core.freelance

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FreelanceAdapter(private var repository: FreelanceJpaRepository) : IFreelanceAdapter {

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

    override fun update(freelance: FreelanceDomain): FreelanceDomain {
        val mewEntity = toEntity(freelance)
        val freelanceEntity = repository.findByUsername(freelance.username).stream().findFirst()

        freelanceEntity.ifPresentOrElse(
            {
                mewEntity.id = it.id
                repository.flush()
            },
            {
                logger.error("unable to find a freelance with username ${freelance.username}")
                throw SignupNotFoundException("unable to find a freelance with username ${freelance.username}")
            }
        )

        return fromEntity(freelanceEntity.get())
    }

    class SignupNotFoundException(private val msg: String) : Throwable(msg) {}
}





