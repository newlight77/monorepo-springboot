package io.oneprofile.core.command

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class CommandRepositoryAdapter(private var repository: CommandJpaRepository) : CommandDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(command: CommandDomain): CommandDomain {
        repository.findByName(command.companyName).stream().findFirst().ifPresent {
            logger.error("a command with companyName ${command.companyName} is already taken")
            throw DuplicateKeyException("a command with companyName ${command.companyName} is already taken")
        }
        val entity = repository.save(toEntity(command))
        entity.lastDate = command.lastDate ?: Instant.now()
        return fromEntity(entity)
    }

    override fun findAll(): List<CommandDomain> {
        return repository.findAll().map {
            fromEntity(it)
        }
    }

    override fun findByName(companyName: String): Optional<CommandDomain> {
        val entity = repository.findByName(companyName).stream().findFirst()
        return entity.map {
            fromEntity(it)
        }
    }

    override fun update(command: CommandDomain): CommandDomain {
        val entity = repository.findByName(command.companyName).stream().findFirst()
        if (entity.isEmpty) throw NotFoundException("The command is not found for companyName ${command.companyName}")
        return entity.map {
            val newEntity = toEntity(command)
            newEntity.id = it.id
            newEntity.contact?.id = it.contact?.id
            newEntity.address?.id = it.address?.id
            newEntity.lastDate = it.lastDate ?: Instant.now()
            val updated = repository.save(newEntity)
            fromEntity(updated)
        }.orElseThrow()
    }

}





