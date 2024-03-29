package io.oneprofile.signup.notification

class NotificationEvent (
        var emailNotification: EmailNotificationDomain? = null,
        var smsNotification: SmsNotificationDomain? = null
) {
        fun isEmail() = emailNotification != null
        fun isSms() = smsNotification != null
}