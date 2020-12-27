package io.tricefal.core.notification

class NotificationEvent (
        var emailContactNotification: EmailContactNotificationDomain? = null,
        var emailFeedbackNotification: EmailFeedbackNotificationDomain? = null,
        var smsNotification: SmsNotificationDomain? = null) {

        fun isEmailContact() = emailContactNotification != null
        fun isEmailFeedback() = emailFeedbackNotification != null
        fun isSms() = smsNotification != null
}