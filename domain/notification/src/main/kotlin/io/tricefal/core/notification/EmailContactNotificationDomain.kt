package io.tricefal.core.notification


class EmailContactNotificationDomain
private constructor(
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

        fun build() = EmailContactNotificationDomain(
            emailFrom = emailFrom,
            emailTo = emailTo,
            lastname = lastname,
            firstname = firstname,
            emailContent = emailContent
        )
    }
}
