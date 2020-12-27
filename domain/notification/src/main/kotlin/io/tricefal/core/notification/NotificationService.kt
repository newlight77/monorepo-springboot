package io.tricefal.core.notification

import org.slf4j.LoggerFactory

class NotificationService(private var adapter: INotificationAdapter) : INotificationService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendEmailContact(notification: EmailContactNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            val emailSignupNotification: EmailNotificationDomain = toEmail(notification, metaNotification)
            adapter.sendEmail(emailSignupNotification)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email for contact ${notification}")
            throw SignupEmailNotificationException("failed to send an email for contact ${notification}", ex)
        }
    }

    private fun toEmail(notification: EmailContactNotificationDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        return EmailNotificationDomain.Builder("")
            .emailContent(notification.emailContent)
            .emailFrom(notification.emailFrom)
            .emailTo(metaNotification.emailAdmin)
            .emailSubject("contact from ${notification.firstname} ${notification.lastname}")
            .build()
    }

    override fun sendEmailFeedback(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            val emailSignupNotification: EmailNotificationDomain = toEmail(notification, metaNotification)
            adapter.sendEmail(emailSignupNotification)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email for feedback for username ${notification}")
            throw SignupEmailNotificationException("failed to send an email for feedback ${notification}", ex)
        }
    }

    private fun toEmail(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        return EmailNotificationDomain.Builder("")
            .emailContent(notification.emailContent)
            .emailFrom(notification.emailFrom)
            .emailTo(metaNotification.emailAdmin)
            .emailSubject("feeedback from ${notification.firstname} ${notification.lastname}")
            .build()
    }

    override fun sendSms(notification: SmsNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            notification.smsTo = metaNotification.smsAdminNumber
            return adapter.sendSms(notification)
        } catch (ex: Throwable) {
            logger.error("failed to send an sms for activation for username ${notification.username}")
            throw SignupSmsNotificationException("failed to send an sms for activation for username ${notification.username}", ex)
        }
    }
}

class SignupEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupSmsNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

