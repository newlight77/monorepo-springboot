package io.oneprofile.core.notification

interface INotificationAdapter {
    fun sendEmail(notification: EmailNotificationDomain): Boolean
    fun sendSms(notification: SmsNotificationDomain): Boolean
}