package io.tricefal.core.notification

import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.email.EmailTemplate
import io.tricefal.core.twilio.SmsMessage
import io.tricefal.core.twilio.SmsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class NotificationAdapter(val mailService: EmailService,
                          val smsService: SmsService) : INotificationAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendSms(notification: SmsNotificationDomain): Boolean {
        logger.info("Sending ans SMS")
        val result = try {
            val message = SmsMessage.Builder()
                .from(notification.smsFrom!!)
                .to(notification.smsTo!!)
                .content(notification.smsContent!!)
                .build()
            smsService.send(message).matches(Regex("^SM[a-z0-9]*"))
        } catch (ex: Exception) {
            logger.error("Failed to send a sms notification for user ${notification.smsTo}")
            throw SmsNotificationException("Failed to send a sms notification for number ${notification.smsTo}")
        }
        logger.info("An SMS has been sent")
        return result
    }

    override fun sendEmail(signupNotification: EmailNotificationDomain): Boolean {
        logger.info("Sending an email")
        try {
            val message = EmailMessage.Builder()
                .from(signupNotification.emailFrom!!)
                .to(signupNotification.emailTo!!)
                .subject(signupNotification.emailSubject!!)
                .content(signupNotification.emailContent!!)
                .emailTemplate(EmailTemplate.SIGNUP)
                .model(hashMapOf("greeting" to signupNotification.emailGreeting!!, "content" to signupNotification.emailContent!!))
                .build()
            mailService.send(message)
        } catch (ex: Exception) {
            logger.error("Failed to send an email notification for user ${signupNotification.emailTo}")
            throw EmailNotificationException("Failed to send an email notification for user ${signupNotification.emailTo}")
        }
        logger.info("An Email has been sent")
        return true
    }

}

class EmailNotificationException(private val msg: String) : Throwable(msg) {}
class SmsNotificationException(private val msg: String) : Throwable(msg) {}






