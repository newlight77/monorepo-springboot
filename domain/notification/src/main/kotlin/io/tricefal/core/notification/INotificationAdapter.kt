package io.tricefal.core.notification

interface INotificationAdapter {
    fun sendEmail(notification: EmailNotificationDomain): Boolean
    fun sendSms(notification: SmsNotificationDomain): Boolean
}