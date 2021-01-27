package io.tricefal.core.company

import io.tricefal.core.freelance.CompanyDomain
import io.tricefal.core.freelance.CompanyEntity
import io.tricefal.core.freelance.fromEntity
import io.tricefal.core.freelance.toEntity
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
class CompanyRepositoryAdapter(private var repository: CompanyJpaRepository,
                               val notificationAdapter: INotificationAdapter,
                               val eventPublisher: CompanyEventPublisher) : CompanyDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(company: CompanyDomain): CompanyDomain {
        repository.findByName(company.raisonSocial).stream().findFirst().ifPresent {
            logger.error("a company with companyName ${company.nomCommercial} is already taken")
            throw DuplicateKeyException("a company with companyName ${company.nomCommercial} is already taken")
        }
        val entity = repository.save(toEntity(company))
        entity.lastDate = company.lastDate ?: Instant.now()
        return fromEntity(entity)
    }

    override fun findAll(): List<CompanyDomain> {
        return repository.findAll().map {
            fromEntity(it)
        }
    }

    override fun findByName(companyName: String): Optional<CompanyDomain> {
        val entity = repository.findByName(companyName).stream().findFirst()
        return entity.map {
            fromEntity(it)
        }
    }

    override fun update(companyName: String, company: CompanyDomain): CompanyDomain {
        val entity = repository.findByName(companyName).stream().findFirst()
        if (entity.isEmpty) throw NotFoundException("The company is not found for companyName ${company.raisonSocial}")
        return entity.map {
            val newEntity = toEntity(company)
            newEntity.id = it.id
            newEntity.id = it.id
            newEntity.pdgContact?.id = it.pdgContact?.id
            newEntity.adminContact?.id = it.adminContact?.id
            newEntity.bankInfo?.id = it.bankInfo?.id
            newEntity.fiscalAddress?.id = it.fiscalAddress?.id
            newEntity.state?.id = it.state?.id
            newEntity.lastDate = it.lastDate ?: Instant.now()
            val updated = repository.save(newEntity)
            fromEntity(updated)
        }.orElseThrow()
    }

    override fun sendEmail(companyName: String, notification: EmailNotificationDomain): Boolean {
        this.eventPublisher.publishEmailNotification(notification)
        return true
        //return notificationAdapter.sendEmail(companyCompletionNotification)
    }

    override fun companyCompleted(companyName: String) {
        eventPublisher.publishCompanyCompletedEvent(companyName)
    }

}





