package io.tricefal.core.notification


class EmailNotificationDomain
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

        fun build() = EmailNotificationDomain(
                username = username,
                emailFrom = emailFrom,
                emailTo = emailTo,
                emailSubject = emailSubject,
                emailContent = emailContent,
                emailGreeting = emailGreeting
        )
    }
}
