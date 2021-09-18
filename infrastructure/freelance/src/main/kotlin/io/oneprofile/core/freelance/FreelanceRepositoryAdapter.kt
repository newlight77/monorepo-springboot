package io.oneprofile.core.freelance

import io.oneprofile.core.notification.EmailNotificationDomain
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class FreelanceRepositoryAdapter(private var repository: FreelanceJpaRepository,
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
        return entity.map { currentEntity ->
            val newEntity = toEntity(freelance)
            newEntity.id = currentEntity.id
            newEntity.company?.id = currentEntity.company?.id
            newEntity.company?.pdgContact?.id = currentEntity.company?.pdgContact?.id
            newEntity.company?.pdgPrivacyDetail?.id = currentEntity.company?.pdgPrivacyDetail?.id
            newEntity.company?.adminContact?.id = currentEntity.company?.adminContact?.id
            newEntity.company?.bankInfo?.id = currentEntity.company?.bankInfo?.id
            newEntity.company?.fiscalAddress?.id = currentEntity.company?.fiscalAddress?.id
            newEntity.company?.state?.id = currentEntity.company?.state?.id
            newEntity.company?.documents?.id = currentEntity.company?.documents?.id
            newEntity.contact?.id = currentEntity.contact?.id
            newEntity.address?.id = currentEntity.address?.id
            newEntity.privacyDetail?.id = currentEntity.privacyDetail?.id
            newEntity.state?.id = currentEntity.state?.id
            newEntity.lastDate = currentEntity.lastDate ?: Instant.now()
            val updated = repository.save(newEntity)
            fromEntity(updated)
        }.orElseThrow()
    }

    // TODO : use event publisher and listener for persistence
//    override fun patch(freelance: FreelanceDomain, operations: List<PatchOperation>): Optional<FreelanceDomain> {
//        val entity = repository.findByUsername(freelance.username).stream().findFirst()
//        if (entity.isEmpty) throw NotFoundException("The freelance is not found for username ${freelance.username}")
//        var updated = Optional.empty<FreelanceDomain>()
//        entity.ifPresent {
//            updated = applyPatch(operations, it)
//        }
//        return updated
//    }

    override fun sendEmail(notification: EmailNotificationDomain): Boolean {
        this.eventPublisher.publishEmailNotification(notification)
        return true
        //return notificationAdapter.sendEmail(companyCompletionNotification)
    }

    override fun companyCompleted(username: String, companyName: String) {
        eventPublisher.publishCompanyCompletedEvent(username, companyName)
    }

//    private fun applyPatch(
//        operations: List<PatchOperation>,
//        entity: FreelanceEntity,
//    ): Optional<FreelanceDomain> {
//        if (entity.company == null) entity.company = CompanyEntity(null,"NONE")
//        if (entity.company?.pdgContact == null) entity.company?.pdgContact = ContactEntity()
//        if (entity.company?.adminContact == null) entity.company?.adminContact = ContactEntity()
//        if (entity.company?.bankInfo == null) entity.company?.bankInfo = BankInfoEntity()
//        if (entity.company?.fiscalAddress == null) entity.company?.fiscalAddress = AddressEntity()
//        if (entity.contact == null) entity.contact = ContactEntity()
//        if (entity.address == null) entity.address = AddressEntity()
//        if (entity.privacyDetail == null) entity.privacyDetail = PrivacyDetailEntity()
//        if (entity.state == null) entity.state = FreelanceStateEntity(username = entity.username)
//
//        return operations.let { ops ->
//            var patched = JsonPatchOperator().apply(entity, ops)
//            patched.lastDate = entity.lastDate ?: Instant.now()
//            patched = repository.save(patched)
//            Optional.of(fromEntity(patched))
//        }
//    }

}





