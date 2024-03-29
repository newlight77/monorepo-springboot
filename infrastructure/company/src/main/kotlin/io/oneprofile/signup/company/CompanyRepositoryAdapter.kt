package io.oneprofile.signup.company

import io.oneprofile.signup.notification.EmailNotificationDomain
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class CompanyRepositoryAdapter(private var repository: CompanyJpaRepository,
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
            newEntity.pdgPrivacyDetail?.id = it.pdgPrivacyDetail?.id
            newEntity.adminContact?.id = it.adminContact?.id
            newEntity.bankInfo?.id = it.bankInfo?.id
            newEntity.fiscalAddress?.id = it.fiscalAddress?.id
            newEntity.state?.id = it.state?.id
            newEntity.documents?.id = it.documents?.id
            newEntity.lastDate = it.lastDate ?: Instant.now()
            val updated = repository.save(newEntity)
            fromEntity(updated)
        }.orElseThrow()
    }

    override fun sendEmail(notification: EmailNotificationDomain): Boolean {
        this.eventPublisher.publishEmailNotification(notification)
        return true
        //return notificationAdapter.sendEmail(companyCompletionNotification)
    }

    override fun companyCompleted(username: String, companyName: String) {
        eventPublisher.publishCompanyCompletedEvent(username, companyName)
    }

}





