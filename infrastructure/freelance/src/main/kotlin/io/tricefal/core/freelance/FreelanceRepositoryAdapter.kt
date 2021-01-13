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
        return repository.findByAvailability(Availability.AVAILABLE.toString()) // may return AVAILABLE_SOON
                .filter { Availability.AVAILABLE.toString() == it.availability?.toUpperCase() }
                .map { fromEntity(it) }
    }

    override fun findByUsername(username: String): Optional<FreelanceDomain> {
        val entity = repository.findByUsername(username).stream().findFirst()
        return entity.map {
            fromEntity(it)
        }
    }

    override fun update(freelance: FreelanceDomain): FreelanceDomain {
        val entity = repository.findByUsername(freelance.username).stream().findFirst()
        if (entity.isEmpty) throw NotFoundException("The freelance is not found for username ${freelance.username}")
        return entity.map {
            val newEntity = toEntity(freelance)
            newEntity.id = it.id
            newEntity.company?.id = it.company?.id
            newEntity.company?.adminContact?.id = it.company?.adminContact?.id
            newEntity.company?.bankInfo?.id = it.company?.bankInfo?.id
            newEntity.company?.fiscalAddress?.id = it.company?.fiscalAddress?.id
            newEntity.contact?.id = it.contact?.id
            newEntity.contact?.address?.id = it.contact?.address?.id
            newEntity.privacyDetail?.id = it.privacyDetail?.id
            newEntity.state?.id = it.state?.id
            newEntity.lastDate = it.lastDate ?: Instant.now()
            val updated = repository.save(newEntity)
            fromEntity(updated)
        }.orElseThrow()
    }

    // TODO : use event publisher and listener for persistence
    override fun patch(freelance: FreelanceDomain, operations: List<PatchOperation>): Optional<FreelanceDomain> {
        val entity = repository.findByUsername(freelance.username).stream().findFirst()
        if (entity.isEmpty) throw NotFoundException("The freelance is not found for username ${freelance.username}")
        var updated = Optional.empty<FreelanceDomain>()
        entity.ifPresent {
            updated = applyPatch(operations, it)
        }
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
        if (entity.company?.bankInfo == null) entity.company?.bankInfo = BankInfoEntity()
        if (entity.company?.fiscalAddress == null) entity.company?.fiscalAddress = AddressEntity()
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





