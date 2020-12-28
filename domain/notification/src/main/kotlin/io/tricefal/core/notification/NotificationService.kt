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

    override fun sendEmailFeedback(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            val emailSignupNotification: EmailNotificationDomain = toEmail(notification, metaNotification)
            return adapter.sendEmail(emailSignupNotification)
        } catch (ex: Throwable) {
            logger.error("failed to send an email for feedback for username ${notification.emailTo}")
            throw SignupEmailNotificationException("failed to send an email for feedback ${notification.emailTo}", ex)
        }
    }

    override fun sendSms(notification: SmsNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            notification.smsTo = metaNotification.smsAdminNumber
            return adapter.sendSms(notification)
        } catch (ex: Throwable) {
            logger.error("failed to send an sms for activation for username ${notification.smsTo}")
            throw SignupSmsNotificationException("failed to send an sms for activation for username ${notification.smsTo}", ex)
        }
    }
}

class SignupEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupSmsNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

