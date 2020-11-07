package io.tricefal.core.notification

class NotificationDomain
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

        fun build() = NotificationDomain(
                username = username,
                smsFrom = smsFrom,
                smsTo = smsTo,
                smsContent = smsContent,
                emailFrom = emailFrom,
                emailTo = emailTo,
                emailSubject = emailSubject,
                emailContent = emailContent,
                emailGreeting = emailGreeting
        )
    }
}