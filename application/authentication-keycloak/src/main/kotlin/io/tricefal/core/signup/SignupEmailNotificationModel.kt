package io.tricefal.core.signup


class SignupEmailNotificationModel
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

        fun build() = SignupEmailNotificationModel(username, emailFrom, emailTo, emailSubject, emailContent, emailGreeting)
    }
}

//class SmsNotificationModel
//private constructor(
//        val username: String,
//        var smsFrom: String? = null,
//        var smsTo: String? = null,
//        var smsContent: String? = null
//) {
//
//    data class Builder (
//            val username: String,
//            var smsFrom: String? = null,
//            var smsTo: String? = null,
//            var smsContent: String? = null
//    ) {
//
//        fun smsFrom(smsFrom: String?) = apply { this.smsFrom = smsFrom }
//        fun smsTo(smsTo: String?) = apply { this.smsTo = smsTo }
//        fun smsContent(smsContent: String?) = apply { this.smsContent = smsContent }
//
//        fun build() = SmsNotificationModel(username, smsFrom, smsTo, smsContent)
//    }
//}

fun toModel(domainSignup: SignupEmailNotificationDomain): SignupEmailNotificationModel {
    return SignupEmailNotificationModel.Builder(domainSignup.username)
            .emailFrom(domainSignup.emailFrom)
            .emailTo(domainSignup.emailTo)
            .emailSubject(domainSignup.emailSubject)
            .emailContent(domainSignup.emailContent)
            .emailGreeting(domainSignup.emailGreeting)
            .build()
}

fun fromModel(modelSignupEmail: SignupEmailNotificationModel): SignupEmailNotificationDomain {
    return SignupEmailNotificationDomain.Builder(modelSignupEmail.username)
            .emailFrom(modelSignupEmail.emailFrom)
            .emailTo(modelSignupEmail.emailTo)
            .emailSubject(modelSignupEmail.emailSubject)
            .emailContent(modelSignupEmail.emailContent)
            .emailGreeting(modelSignupEmail.emailGreeting)
            .build()
}

//fun toModel(domain: SmsNotificationDomain): SmsNotificationModel {
//    return SmsNotificationModel.Builder(domain.username)
//            .smsFrom(domain.smsFrom)
//            .smsTo(domain.smsTo)
//            .smsContent(domain.smsContent)
//            .build()
//}
//
//fun fromModel(model: SmsNotificationModel): SmsNotificationDomain {
//    return SmsNotificationDomain.Builder(model.username)
//            .smsFrom(model.smsFrom)
//            .smsTo(model.smsTo)
//            .smsContent(model.smsContent)
//            .build()
//}