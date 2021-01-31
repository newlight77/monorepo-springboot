package io.tricefal.core.company

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.NotificationEvent
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

    fun publishEmailNotification(notification: EmailNotificationDomain) {
        try {
            applicationEventPublisher.publishEvent(
                NotificationEvent(notification)
            )
            logger.info("An EmailNotificationEvent has been published to ${notification.emailTo} ")
        } catch (ex: Exception) {
            logger.error("Failed to publish a EmailNotificationEvent to ${notification.emailTo}")
            throw EmailNotifiicationPublicationException("Failed to publish a EmailNotificationEvent to ${notification.emailTo}")
        }
    }

}

class CompanyCompletionPublicationException(private val msg: String) : Throwable(msg) {}
class EmailNotifiicationPublicationException(private val msg: String) : Throwable(msg) {}



