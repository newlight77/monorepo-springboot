package io.tricefal.core.company

import io.tricefal.core.signup.CompanyCompletionEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CompanyEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishCompanyCompletedEvent(companyName: String) = try {
        applicationEventPublisher.publishEvent(
                CompanyCompletionEvent(companyName)
        )
        logger.info("A CompanyCompletionEvent has been published user $companyName")
    } catch (ex: Exception) {
        logger.error("Failed to publish a CompanyCompletionEvent for user $companyName")
        throw CompanyCompletionPublicationException("Failed to publish a CompanyCompletionEvent for user $companyName")
    }

}

class CompanyCompletionPublicationException(private val msg: String) : Throwable(msg) {}



