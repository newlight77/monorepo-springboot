package io.tricefal.core.notification


interface INotificationService {
    fun sendEmailContact(notification: EmailContactNotificationDomain, metaNotification: MetaNotificationDomain): Boolean
    fun sendEmailFeedback(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): Boolean
    fun sendSms(notification: SmsNotificationDomain, metaNotification: MetaNotificationDomain): Boolean
}