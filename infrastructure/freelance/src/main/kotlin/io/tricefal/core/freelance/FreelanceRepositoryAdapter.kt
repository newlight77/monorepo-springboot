package io.tricefal.core.freelance

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.INotificationAdapter
import io.tricefal.shared.util.json.JsonPatchOperator
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class FreelanceRepositoryAdapter(private var repository: FreelanceJpaRepository,
                                 val notificationAdapter: INotificationAdapter,
                                 val eventPublisher: FreelanceEventPublisher) : FreelanceDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        repository.findByUsername(freelance.username).stream().findFirst().ifPresent {
            logger.error("a freelance with username ${freelance.username} is already taken")
            throw DuplicateKeyException("a freelance with username ${freelance.username} is already taken")
        }
        val entity = repository.save(toEntity(freelance))
        entity.lastDate = freelance.lastDate ?: Instant.now()
        return fromEntity(entity)
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
                val newEntity = toEntity(freelance)
                newEntity.id = it.id
                newEntity.company?.id = it.company?.id
                newEntity.contact?.id = it.contact?.id
                newEntity.privacyDetail?.id = it.privacyDetail?.id
                newEntity.state?.id = it.state?.id
                newEntity.lastDate = it.lastDate ?: Instant.now()
                repository.save(newEntity)
                updated =  Optional.of(fromEntity(newEntity))
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
                updated = applyPatch(operations, it)
            },
            {
                logger.error("unable to find a freelance with username ${freelance.username}")
                throw FreelanceNotFoundException("unable to find a freelance with username ${freelance.username}")
            }
        )
        return updated
    }

    override fun sendEmail(username: String, companyCompletionNotification: EmailNotificationDomain): Boolean {
        return notificationAdapter.sendEmail(companyCompletionNotification)
    }

    override fun companyCompleted(username: String) {
        eventPublisher.publishCompanyCompletedEvent(username)
    }

    private fun applyPatch(
        operations: List<PatchOperation>,
        entity: FreelanceEntity,
    ): Optional<FreelanceDomain> {
        if (entity.company == null) entity.company = CompanyEntity()
        if (entity.company?.adminContact == null) entity.company?.adminContact = ContactEntity()
        if (entity.company?.adminContact?.address == null) entity.company?.adminContact?.address = AddressEntity()
        if (entity.contact == null) entity.contact = ContactEntity()
        if (entity.contact?.address == null) entity.contact?.address = AddressEntity()
        if (entity.privacyDetail == null) entity.privacyDetail = PrivacyDetailEntity(username = entity.username)
        if (entity.state == null) entity.state = FreelanceStateEntity(username = entity.username)

        return operations.let { ops ->
            var patched = JsonPatchOperator().apply(entity, ops)
            patched.lastDate = entity.lastDate ?: Instant.now()
            patched = repository.save(patched)
            Optional.of(fromEntity(patched))
        }
    }

    class FreelanceNotFoundException(private val msg: String) : Throwable(msg) {}
}





