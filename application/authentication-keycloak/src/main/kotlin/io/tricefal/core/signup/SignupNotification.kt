package io.tricefal.core.signup

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain


class EmailNotificationModel
    private constructor(
        val username: String,
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var emailSubject: String? = null,
        var emailContent: String? = null,
        var emailGreeting: String? = null) {

    data class Builder (
            val username: String,
            var emailFrom: String? = null,
            var emailTo: String? = null,
            var emailSubject: String? = null,
            var emailContent: String? = null,
            var emailGreeting: String? = null) {

        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
        fun emailSubject(emailSubject: String?) = apply { this.emailSubject = emailSubject }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailGreeting(emailGreeting: String?) = apply { this.emailGreeting = emailGreeting }

        fun build() = EmailNotificationModel(username, emailFrom, emailTo, emailSubject, emailContent, emailGreeting)
    }
}

class SmsNotificationModel
private constructor(
        val username: String,
        var smsFrom: String? = null,
        var smsTo: String? = null,
        var smsContent: String? = null
) {

    data class Builder (
            val username: String,
            var smsFrom: String? = null,
            var smsTo: String? = null,
            var smsContent: String? = null
    ) {

        fun smsFrom(smsFrom: String?) = apply { this.smsFrom = smsFrom }
        fun smsTo(smsTo: String?) = apply { this.smsTo = smsTo }
        fun smsContent(smsContent: String?) = apply { this.smsContent = smsContent }

        fun build() = SmsNotificationModel(username, smsFrom, smsTo, smsContent)
    }
}

fun toModel(domain: EmailNotificationDomain): EmailNotificationModel {
    return EmailNotificationModel.Builder(domain.username)
            .emailFrom(domain.emailFrom)
            .emailTo(domain.emailTo)
            .emailSubject(domain.emailSubject)
            .emailContent(domain.emailContent)
            .emailGreeting(domain.emailGreeting)
            .build()
}

fun fromModel(model: EmailNotificationModel): EmailNotificationDomain {
    return EmailNotificationDomain.Builder(model.username)
            .emailFrom(model.emailFrom)
            .emailTo(model.emailTo)
            .emailSubject(model.emailSubject)
            .emailContent(model.emailContent)
            .emailGreeting(model.emailGreeting)
            .build()
}

fun toModel(domain: SmsNotificationDomain): SmsNotificationModel {
    return SmsNotificationModel.Builder(domain.username)
            .smsFrom(domain.smsFrom)
            .smsTo(domain.smsTo)
            .smsContent(domain.smsContent)
            .build()
}

fun fromModel(model: SmsNotificationModel): SmsNotificationDomain {
    return SmsNotificationDomain.Builder(model.username)
            .smsFrom(model.smsFrom)
            .smsTo(model.smsTo)
            .smsContent(model.smsContent)
            .build()
}