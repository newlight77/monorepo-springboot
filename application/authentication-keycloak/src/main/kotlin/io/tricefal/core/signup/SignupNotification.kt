package io.tricefal.core.signup

import io.tricefal.core.notification.NotificationDomain

class SignupNotificationModel
    private constructor(
        val username: String,
        var smsFrom: String? = null,
        var smsTo: String? = null,
        var smsContent: String? = null,
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var emailSubject: String? = null,
        var emailContent: String? = null,
        var emailGreeting: String? = null) {

    data class Builder (
            val username: String,
            var smsFrom: String? = null,
            var smsTo: String? = null,
            var smsContent: String? = null,
            var emailFrom: String? = null,
            var emailTo: String? = null,
            var emailSubject: String? = null,
            var emailContent: String? = null,
            var emailGreeting: String? = null) {

        fun smsFrom(smsFrom: String?) = apply { this.smsFrom = smsFrom }
        fun smsTo(smsTo: String?) = apply { this.smsTo = smsTo }
        fun smsContent(smsContent: String?) = apply { this.smsContent = smsContent }
        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
        fun emailSubject(emailSubject: String?) = apply { this.emailSubject = emailSubject }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailGreeting(emailGreeting: String?) = apply { this.emailGreeting = emailGreeting }

        fun build() = SignupNotificationModel(username, smsFrom, smsTo, smsContent, emailFrom, emailTo, emailSubject, emailContent, emailGreeting)
    }
}

fun toModel(domain: NotificationDomain): SignupNotificationModel {
    return SignupNotificationModel.Builder(domain.username)
            .smsFrom(domain.smsFrom)
            .smsTo(domain.smsTo)
            .smsContent(domain.smsContent)
            .emailFrom(domain.emailFrom)
            .emailTo(domain.emailTo)
            .emailSubject(domain.emailSubject)
            .emailContent(domain.emailContent)
            .emailGreeting(domain.emailGreeting)
            .build()
}

fun fromModel(model: SignupNotificationModel): NotificationDomain {
    return NotificationDomain.Builder(model.username)
            .smsFrom(model.smsFrom)
            .smsTo(model.smsTo)
            .smsContent(model.smsContent)
            .emailFrom(model.emailFrom)
            .emailTo(model.emailTo)
            .emailSubject(model.emailSubject)
            .emailContent(model.emailContent)
            .emailGreeting(model.emailGreeting)
            .build()
}