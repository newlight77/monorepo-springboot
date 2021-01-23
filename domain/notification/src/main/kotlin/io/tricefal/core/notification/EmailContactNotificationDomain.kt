package io.tricefal.core.notification


data class EmailContactNotificationDomain
    constructor(
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var lastname: String? = null,
        var firstname: String? = null,
        var emailContent: String? = null,
        var emailSignature: String? = null,
    ) {

    data class Builder (
            var emailFrom: String? = null,
            var emailTo: String? = null,
            var lastname: String? = null,
            var firstname: String? = null,
            var emailContent: String? = null,
            var emailSignature: String? = null,
    ) {

        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailSignature(emailSignature: String?) = apply { this.emailSignature = emailSignature }

        fun build() = EmailContactNotificationDomain(
            emailFrom = emailFrom,
            emailTo = emailTo,
            lastname = lastname,
            firstname = firstname,
            emailContent = emailContent,
            emailSignature = emailSignature
        )
    }
}

fun toEmail(notification: EmailContactNotificationDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
    return EmailNotificationDomain.Builder("")
        .emailContent(notification.emailContent)
        .emailFrom(notification.emailFrom)
        .emailTo(metaNotification.emailAdmin)
        .emailSubject("contact from ${notification.firstname} ${notification.lastname}")
        .emailSignature(notification.emailSignature)
        .build()
}
