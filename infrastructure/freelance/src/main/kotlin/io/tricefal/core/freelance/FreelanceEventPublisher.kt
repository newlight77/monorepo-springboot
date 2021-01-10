package io.tricefal.core.freelance

import io.tricefal.core.signup.CompanyCompletionEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class FreelanceEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishCompanyCompletedEvent(username: String) = try {
        applicationEventPublisher.publishEvent(
                CompanyCompletionEvent(username)
        )
        logger.info("A CompanyCompletionEvent has been published user $username")
    } catch (ex: Exception) {
        logger.error("Failed to publish a CompanyCompletionEvent for user $username")
        throw CompanyCompletionPublicationException("Failed to publish a CompanyCompletionEvent for user $username")
    }

}

class CompanyCompletionPublicationException(private val msg: String) : Throwable(msg) {}



