package io.oneprofile.signup.notification

import org.slf4j.LoggerFactory

class NotificationService(private var adapter: INotificationAdapter) : INotificationService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendEmailContact(notification: EmailContactNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            val emailNotification: EmailNotificationDomain = toEmail(notification, metaNotification)
            adapter.sendEmail(emailNotification)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email for contact $notification", ex)
            throw ContactEmailNotificationException("failed to send an email for contact $notification", ex)
        }
    }

    override fun sendEmailFeedback(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            val emailNotification: EmailNotificationDomain = toEmail(notification, metaNotification)
            return adapter.sendEmail(emailNotification)
        } catch (ex: Throwable) {
            logger.error("failed to send an email for feedback for username ${notification.emailTo}", ex)
            throw FeedbackEmailNotificationException("failed to send an email for feedback ${notification.emailTo}", ex)
        }
    }

    override fun sendEmail(notification: EmailNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            return adapter.sendEmail(notification)
        } catch (ex: Throwable) {
            logger.error("failed to send an email to ${notification.emailTo}", ex)
            throw SignupEmailNotificationException("failed to send an email to ${notification.emailTo}", ex)
        }
    }

    override fun sendSms(notification: SmsNotificationDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            notification.smsTo = metaNotification.smsAdminNumber
            return adapter.sendSms(notification)
        } catch (ex: Throwable) {
            logger.error("failed to send an sms for activation for username ${notification.smsTo}", ex)
            throw SignupSmsNotificationException("failed to send an sms for activation for username ${notification.smsTo}", ex)
        }
    }
}

class ContactEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class FeedbackEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupSmsNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

