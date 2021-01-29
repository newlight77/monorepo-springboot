package io.tricefal.core.notification


class EmailContactNotificationModel
private constructor(
        var firstname: String? = null,
        var lastname: String? = null,
        var emailFrom: String? = null,
        var emailSubject: String? = null,
        var emailContent: String? = null,
        var emailSignature: String? = null,
        var phoneNumber: String? = null,
) {

    data class Builder (
            var firstname: String? = null,
            var lastname: String? = null,
            var emailFrom: String? = null,
            var emailSubject: String? = null,
            var emailContent: String? = null,
            var emailSignature: String? = null,
            var phoneNumber: String? = null,
    ) {

        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailSubject(emailSubject: String?) = apply { this.emailContent = emailSubject }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailSignature(emailSignature: String?) = apply { this.emailSignature = emailSignature }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }

        fun build() = EmailContactNotificationModel(
            firstname = firstname,
            lastname = lastname,
            emailFrom = emailFrom,
            emailSubject = emailSubject,
            emailContent = emailContent,
            emailSignature = emailSignature,
            phoneNumber = phoneNumber,
        )
    }
}

fun toModel(domain: EmailContactNotificationDomain): EmailContactNotificationModel {
    return EmailContactNotificationModel.Builder()
        .firstname(domain.firstname)
        .lastname(domain.lastname)
        .emailFrom(domain.emailFrom)
        .emailSubject(domain.emailSubject)
        .emailContent(domain.emailContent)
        .emailSignature(domain.emailSignature)
        .phoneNumber(domain.phoneNumber)
        .build()
}

fun fromModel(model: EmailContactNotificationModel): EmailContactNotificationDomain {
    return EmailContactNotificationDomain.Builder()
        .emailFrom(model.emailFrom)
        .firstname(model.firstname)
        .lastname(model.lastname)
        .emailSubject(model.emailSubject)
        .emailContent(model.emailContent)
        .emailSignature(model.emailSignature)
        .phoneNumber(model.phoneNumber)
        .build()
}