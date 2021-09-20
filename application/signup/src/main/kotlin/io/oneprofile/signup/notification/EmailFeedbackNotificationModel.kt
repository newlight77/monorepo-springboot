package io.oneprofile.signup.notification


class EmailFeedbackNotificationModel
private constructor(
    var firstname: String? = null,
    var lastname: String? = null,
    var emailFrom: String? = null,
    var emailSubject: String? = null,
    var emailContent: String? = null,
    var emailSignature: String? = null,
    var phoneNumber: String? = null,
    var origin: String? = null,
) {

    data class Builder (
        var firstname: String? = null,
        var lastname: String? = null,
        var emailFrom: String? = null,
        var emailSubject: String? = null,
        var emailContent: String? = null,
        var emailSignature: String? = null,
        var phoneNumber: String? = null,
        var origin: String? = null,
    ) {

        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun emailSubject(emailSubject: String?) = apply { this.emailSubject = emailSubject }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
        fun emailSignature(emailSignature: String?) = apply { this.emailSignature = emailSignature }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun origin(origin: String?) = apply { this.origin = origin }

        fun build() = EmailFeedbackNotificationModel(
            firstname = firstname,
            lastname = lastname,
            emailFrom = emailFrom,
            emailSubject = emailSubject,
            emailContent = emailContent,
            emailSignature = emailSignature,
            phoneNumber = phoneNumber,
            origin = origin,
        )
    }
}

fun toModel(domain: EmailFeedbackNotificationDomain): EmailFeedbackNotificationModel {
    return EmailFeedbackNotificationModel.Builder()
        .firstname(domain.firstname)
        .lastname(domain.lastname)
        .emailFrom(domain.emailFrom)
        .emailSubject(domain.emailSubject)
        .emailContent(domain.emailContent)
        .emailSignature(domain.emailSignature)
        .phoneNumber(domain.phoneNumber)
        .origin(domain.origin)
        .build()
}

fun fromModel(model: EmailFeedbackNotificationModel): EmailFeedbackNotificationDomain {
    return EmailFeedbackNotificationDomain.Builder()
        .emailFrom(model.emailFrom)
        .firstname(model.firstname)
        .lastname(model.lastname)
        .emailSubject(model.emailSubject)
        .emailContent(model.emailContent)
        .emailSignature(model.emailSignature)
        .phoneNumber(model.phoneNumber)
        .origin(model.origin)
        .build()
}