package io.tricefal.core.notification


class EmailContactNotificationModel
private constructor(
        var emailFrom: String? = null,
        var firstname: String? = null,
        var lastname: String? = null,
        var emailContent: String? = null) {

    data class Builder (
            var emailFrom: String? = null,
            var firstname: String? = null,
            var emailTo: String? = null,
            var lastname: String? = null,
            var emailContent: String? = null) {

        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }

        fun build() = EmailContactNotificationModel(
            emailFrom = emailFrom,
            firstname = firstname,
            lastname = lastname,
            emailContent = emailContent
        )
    }
}

fun toModel(domainContact: EmailContactNotificationDomain): EmailContactNotificationModel {
    return EmailContactNotificationModel.Builder()
        .emailFrom(domainContact.emailFrom)
        .firstname(domainContact.firstname)
        .lastname(domainContact.lastname)
        .emailContent(domainContact.emailContent)
        .build()
}

fun fromModel(model: EmailContactNotificationModel): EmailContactNotificationDomain {
    return EmailContactNotificationDomain.Builder()
        .emailFrom(model.emailFrom)
        .firstname(model.firstname)
        .lastname(model.lastname)
        .emailContent(model.emailContent)
        .build()
}