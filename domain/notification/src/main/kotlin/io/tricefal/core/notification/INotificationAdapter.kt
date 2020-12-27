package io.tricefal.core.notification

interface INotificationAdapter {
    fun sendEmail(signupNotification: EmailNotificationDomain): Boolean
    fun sendSms(notification: SmsNotificationDomain): Boolean
}