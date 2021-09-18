package io.oneprofile.core.company

import io.oneprofile.core.notification.EmailNotificationDomain
import io.oneprofile.core.notification.NotificationEvent
import io.oneprofile.core.signup.CompanyCompletionEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CompanyEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishCompanyCompletedEvent(username: String, companyName: String) = try {
        applicationEventPublisher.publishEvent(
                CompanyCompletionEvent(username, companyName)
        )
        logger.info("A CompanyCompletionEvent has been published user $companyName")
    } catch (ex: Exception) {
        logger.error("Failed to publish a CompanyCompletionEvent for user $companyName", ex)
        throw CompanyCompletionPublicationException("Failed to publish a CompanyCompletionEvent for user $companyName", ex)
    }

    fun publishEmailNotification(notification: EmailNotificationDomain) {
        try {
            applicationEventPublisher.publishEvent(
                NotificationEvent(notification)
            )
            logger.info("An EmailNotificationEvent has been published to ${notification.emailTo} ")
        } catch (ex: Exception) {
            logger.error("Failed to publish a EmailNotificationEvent to ${notification.emailTo}", ex)
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



