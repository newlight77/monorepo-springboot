package io.tricefal.core.notification

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
@PropertySource("classpath:application.yml")
class NotificationWebHandler(val notificationService: INotificationService,
                             private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var emailAdmin = env.getProperty("notification.mail.admin")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!
    private var smsAdmin = env.getProperty("notification.sms.admin")!!

    @Async
    fun sendContactEmail(notification: EmailContactNotificationModel): Boolean {
        return notificationService.sendEmailContact(fromModel(notification) , MetaNotificationDomain(emailFrom = emailFrom, smsFrom = smsFrom))
    }

    @Async
    fun sendFeedbackEmail(notification: EmailFeedbackNotificationModel): Boolean {
        return notificationService.sendEmailFeedback(fromModel(notification), MetaNotificationDomain(emailFrom = emailFrom, smsFrom = smsFrom))
    }

    @Async
    fun sendEmail(notification: EmailNotificationDomain) {
        try {
            val metaNotification = MetaNotificationDomain(baseUrl=backendBaseUrl, emailFrom=emailFrom, emailAdmin=emailAdmin, smsFrom=smsFrom, smsAdminNumber=smsAdmin)
            notificationService.sendEmail(notification, metaNotification)
        } catch (ex: Throwable) {
            logger.error("Failed to send an email to ${notification.emailTo}")
            throw EmailNotificationEventException("Failed to send an email to ${notification.emailTo}",
                ex)
        }
        logger.info("Email successfullly sent to ${notification.emailTo}")

    }

    fun sendSms(notification: SmsNotificationModel): Boolean {
        return notificationService.sendSms(fromModel(notification), MetaNotificationDomain(emailFrom = emailFrom, smsFrom = smsFrom))
    }

}
