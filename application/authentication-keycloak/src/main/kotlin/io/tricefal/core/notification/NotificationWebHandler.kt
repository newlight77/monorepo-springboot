package io.tricefal.core.notification

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service
@PropertySource("classpath:application.yml")
class NotificationWebHandler(val notificationService: INotificationService,
                             private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    fun sendContactEmail(notification: EmailContactNotificationModel): Boolean {
        return notificationService.sendEmailContact(fromModel(notification) , MetaNotificationDomain(emailFrom = emailFrom, smsFrom = smsFrom))
    }

    fun sendFeedbackEmail(notification: EmailFeedbackNotificationModel): Boolean {
        return notificationService.sendEmailFeedback(fromModel(notification), MetaNotificationDomain(emailFrom = emailFrom, smsFrom = smsFrom))
    }

    fun sendSms(notification: SmsNotificationModel): Boolean {
        return notificationService.sendSms(fromModel(notification), MetaNotificationDomain(emailFrom = emailFrom, smsFrom = smsFrom))
    }

}
