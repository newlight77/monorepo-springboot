package io.tricefal.core.freelance

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FreelanceAdapter(private var repository: FreelanceJpaRepository) : IFreelanceAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        repository.findByUsername(freelance.username).ifPresent {
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

    override fun findByUsername(username: String): Optional<FreelanceDomain> {
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }

    override fun update(freelance: FreelanceDomain): FreelanceDomain {
        val entity = toEntity(freelance)
        repository.findByUsername(freelance.username).ifPresent {
            entity.id = it.id
        }
        val freelanceEntity = repository.save(entity)
        return fromEntity(freelanceEntity)
    }

}





