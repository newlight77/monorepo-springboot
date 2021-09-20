package io.oneprofile.signup.notification

import io.oneprofile.signup.email.*
import io.oneprofile.signup.twilio.SmsMessage
import io.oneprofile.signup.twilio.SmsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class NotificationAdapter(val mailService: EmailService,
                          val smsService: SmsService) : INotificationAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendSms(notification: SmsNotificationDomain): Boolean {
        logger.info("Sending ans SMS")
        try {
            val message = SmsMessage.Builder()
                .from(notification.smsFrom!!)
                .to(notification.smsTo!!)
                .content(notification.smsContent!!)
                .build()
            smsService.send(message).matches(Regex("^SM[a-z0-9]*"))
        } catch (ex: Exception) {
            logger.error("Failed to send a sms notification for user ${notification.smsTo}", ex)
            throw SmsNotificationException("Failed to send a sms notification for number ${notification.smsTo}", ex)
        }
        logger.info("An SMS has been sent")
        return true
    }

    override fun sendEmail(notification: EmailNotificationDomain): Boolean {
        logger.info("Sending an email")
        try {
            val message = EmailMessage.Builder()
                .from(notification.emailFrom!!)
                .to(notification.emailTo!!)
                .cc(notification.emailCc)
                .bcc(notification.emailBcc)
                .subject(notification.emailSubject!! + emailSubjectByEnv(notification.targetEnv ?: ""))
                .content(notification.emailContent!!)
                .emailTemplate(emailTemplateByEnv(notification.targetEnv ?: ""))
                .model(hashMapOf(
                    "themeColor" to emailTemplateThemeStyleByEnv(notification.targetEnv ?: "").toString(),
                    "greeting" to notification.emailGreeting!!,
                    "content" to notification.emailContent!!,
                    "signature" to notification.emailSignature!!
                ))
                .build()
            mailService.send(message)
        } catch (ex: Exception) {
            logger.error("Failed to send an email notification for user ${notification.emailTo}", ex)
            throw EmailNotificationException("Failed to send an email notification for user ${notification.emailTo}", ex)
        }
        logger.info("An Email has been sent")
        return true
    }


}

class EmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SmsNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}





