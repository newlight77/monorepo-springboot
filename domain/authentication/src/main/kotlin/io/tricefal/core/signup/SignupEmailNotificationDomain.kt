package io.tricefal.core.signup

import io.tricefal.core.notification.EmailNotificationDomain


class SignupEmailNotificationDomain
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

        fun build() = SignupEmailNotificationDomain(
                username = username,
                emailFrom = emailFrom,
                emailTo = emailTo,
                emailSubject = emailSubject,
                emailContent = emailContent,
                emailGreeting = emailGreeting
        )
    }
}


fun toEmail(signupNotification: SignupEmailNotificationDomain): EmailNotificationDomain {
    return EmailNotificationDomain.Builder(signupNotification.username)
        .emailFrom(signupNotification.emailFrom)
        .emailTo(signupNotification.emailTo)
        .emailSubject(signupNotification.emailSubject)
        .emailContent(signupNotification.emailContent)
        .emailGreeting(signupNotification.emailGreeting).build()
}