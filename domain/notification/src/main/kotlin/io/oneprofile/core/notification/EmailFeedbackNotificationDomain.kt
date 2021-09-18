package io.oneprofile.core.notification


data class EmailFeedbackNotificationDomain
    constructor(
        var lastname: String? = null,
        var firstname: String? = null,
        var emailFrom: String? = null,
        var emailTo: String? = null,
        var emailSubject: String? = null,
        var emailGreeting: String? = null,
        var emailContent: String? = null,
        var emailSignature: String? = null,
        var phoneNumber: String? = null,
        var origin: String? = null,
    ) {

    data class Builder (
            var lastname: String? = null,
            var firstname: String? = null,
            var emailFrom: String? = null,
            var emailTo: String? = null,
            var emailSubject: String? = null,
            var emailGreeting: String? = null,
            var emailContent: String? = null,
            var emailSignature: String? = null,
            var phoneNumber: String? = null,
            var origin: String? = null,
    ) {

        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
        fun emailSubject(emailSubject: String?) = apply { this.emailSubject = emailSubject }
        fun emailGreeting(emailGreeting: String?) = apply { this.emailGreeting = emailGreeting }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailSignature(emailSignature: String?) = apply { this.emailSignature = emailSignature }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun origin(origin: String?) = apply { this.origin = origin }

        fun build() = EmailFeedbackNotificationDomain(
            lastname = lastname,
            firstname = firstname,
            emailFrom = emailFrom,
            emailTo = emailTo,
            emailSubject = emailSubject,
            emailGreeting = emailGreeting,
            emailContent = emailContent,
            emailSignature = emailSignature,
            phoneNumber = phoneNumber,
            origin = origin,
        )
    }
}

fun toEmail(notification: EmailFeedbackNotificationDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
    return EmailNotificationDomain.Builder("")
        .emailFrom(notification.emailFrom)
        .emailTo(metaNotification.emailAdmin)
        .emailSubject("${notification.emailSubject} : de la part de ${notification.firstname} ${notification.lastname}")
        .emailGreeting("la team tricefal®,")
        .emailContent(notification.emailContent)
        .emailSignature("${notification.emailSignature} <br> ${notification.firstname} ${notification.lastname} <br> ${notification.phoneNumber} <br>ip: ${notification.origin}")
        .targetEnv(metaNotification.targetEnv)
        .build()
}
