package io.oneprofile.signup.notification

interface INotificationAdapter {
    fun sendEmail(notification: EmailNotificationDomain): Boolean
    fun sendSms(notification: SmsNotificationDomain): Boolean
}