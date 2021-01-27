package io.tricefal.core.freelance

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.NotificationEvent
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

    fun publishEmailNotification(notification: EmailNotificationDomain) {
        try {
            applicationEventPublisher.publishEvent(
                NotificationEvent(notification)
            )
            logger.info("An EmailNotificationEvent has been published to ${notification.emailTo}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a EmailNotificationEvent to ${notification.emailTo}")
            throw EmailNotifiicationPublicationException("Failed to publish a EmailNotificationEvent to ${notification.emailTo}")
        }
    }

}

class CompanyCompletionPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class EmailNotifiicationPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}


