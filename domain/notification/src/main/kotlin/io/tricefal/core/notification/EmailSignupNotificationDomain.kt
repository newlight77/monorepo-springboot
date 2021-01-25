package io.tricefal.core.notification


data class EmailNotificationDomain
    constructor(
        val username: String,
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var emailCc: String? = null,
        var emailBcc: String? = null,
        var emailSubject: String? = null,
        var emailContent: String? = null,
        var emailGreeting: String? = null,
        var emailSignature: String? = null,
        var emailThemeStyle: String? = null,
        var targetEnv: String? = null,
    ) {


    data class Builder (
        val username: String,
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var emailCc: String? = null,
        var emailBcc: String? = null,
        var emailSubject: String? = null,
        var emailContent: String? = null,
        var emailGreeting: String? = null,
        var emailSignature: String? = null,
        var emailThemeStyle: String? = null,
        var targetEnv: String? = null,
    ) {

        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
        fun emailSubject(emailSubject: String?) = apply { this.emailSubject = emailSubject }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailGreeting(emailGreeting: String?) = apply { this.emailGreeting = emailGreeting }
        fun emailSignature(emailSignature: String?) = apply { this.emailSignature = emailSignature }
        fun emailThemeStyle(emailThemeStyle: String?) = apply { this.emailThemeStyle = emailThemeStyle }
        fun emailCc(emailCc: String?) = apply { this.emailCc = emailCc }
        fun emailBcc(emailBcc: String?) = apply { this.emailBcc = emailBcc }
        fun targetEnv(targetEnv: String?) = apply { this.targetEnv = targetEnv }

        fun build() = EmailNotificationDomain(
            username = username,
            emailFrom = emailFrom,
            emailTo = emailTo,
            emailSubject = emailSubject,
            emailContent = emailContent,
            emailGreeting = emailGreeting,
            emailSignature = emailSignature,
            emailThemeStyle = emailThemeStyle,
            emailCc = emailCc,
            emailBcc = emailBcc,
            targetEnv = targetEnv
        )
    }
}
