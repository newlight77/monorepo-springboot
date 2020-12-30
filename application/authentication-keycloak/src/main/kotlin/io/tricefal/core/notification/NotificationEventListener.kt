package io.tricefal.core.notification

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class NotificationEventListener(val notificationService: INotificationService,
                                private final val env: Environment
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    @EventListener(condition = "#event.isEmailContact()")
    fun handleEmailContactNotificationEvent(event: NotificationEvent) {
        try {
            notificationService.sendEmailContact(event.emailContactNotification!!, MetaNotificationDomain(backendBaseUrl, emailFrom, smsFrom))
        } catch (ex: Throwable) {
            throw CguAcceptedSavingException("Failed to send an email for contact from the notification event ${event.emailContactNotification}", ex)
        }
        logger.info("Failed to send an email for feedback from the notification event ${event.emailContactNotification}")
    }

    @EventListener(condition = "#event.isEmailFeedback()")
    fun handleEmailFeedbackNotificationEvent(event: NotificationEvent) {
        try {
            notificationService.sendEmailContact(event.emailContactNotification!!, MetaNotificationDomain(backendBaseUrl, emailFrom, smsFrom))
        } catch (ex: Throwable) {
            throw CguAcceptedSavingException("Failed to send an email for feedback from the notification event ${event.emailContactNotification}")
        }
        logger.info("Failed to send an email for feedback from the notification event ${event.emailContactNotification}")
    }

    class CguAcceptedSavingException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}