package io.tricefal.core.notification


data class EmailFeedbackNotificationDomain
    constructor(
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var lastname: String? = null,
        var firstname: String? = null,
        var emailContent: String? = null) {

    data class Builder (
            var emailFrom: String? = null,
            var emailTo: String? = null,
            var lastname: String? = null,
            var firstname: String? = null,
            var emailContent: String? = null) {

        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }

        fun build() = EmailFeedbackNotificationDomain(
            emailFrom = emailFrom,
            emailTo = emailTo,
            lastname = lastname,
            firstname = firstname,
            emailContent = emailContent
        )
    }
}

fun toEmail(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
    return EmailNotificationDomain.Builder("")
        .emailContent(notification.emailContent)
        .emailFrom(notification.emailFrom)
        .emailTo(metaNotification.emailAdmin)
        .emailSubject("feeedback from ${notification.firstname} ${notification.lastname}")
        .build()
}
