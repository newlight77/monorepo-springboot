//package io.tricefal.core.notification
//
//
//class EmailSignupNotificationModel
//private constructor(
//        val username: String,
//        var emailFrom: String? = null,
//        var emailTo: String? = null,
//        var emailSubject: String? = null,
//        var emailContent: String? = null) {
//
//    data class Builder (
//            val username: String,
//            var emailFrom: String? = null,
//            var emailTo: String? = null,
//            var emailSubject: String? = null,
//            var emailContent: String? = null) {
//
//        fun emailFrom(emailFrom: String?) = apply { this.emailFrom = emailFrom }
//        fun emailTo(emailTo: String?) = apply { this.emailTo = emailTo }
//        fun emailSubject(emailSubject: String?) = apply { this.emailSubject = emailSubject }
//        fun emailContent(emailContent: String?) = apply { this.emailContent = emailContent }
//
//        fun build() = EmailSignupNotificationModel(
//                username = username,
//                emailFrom = emailFrom,
//                emailTo = emailTo,
//                emailSubject = emailSubject,
//                emailContent = emailContent
//        )
//    }
//}
//
//fun toModel(domainSignup: EmailSignupNotificationDomain): EmailSignupNotificationModel {
//    return EmailSignupNotificationModel.Builder(domainSignup.username)
//        .emailFrom(domainSignup.emailFrom)
//        .emailTo(domainSignup.emailTo)
//        .emailSubject(domainSignup.emailSubject)
//        .emailContent(domainSignup.emailContent)
//        .build()
//}
//
//fun fromModel(modelSiignup: EmailSignupNotificationModel): EmailSignupNotificationDomain {
//    return EmailSignupNotificationDomain.Builder(modelSiignup.username)
//        .emailFrom(modelSiignup.emailFrom)
//        .emailTo(modelSiignup.emailTo)
//        .emailSubject(modelSiignup.emailSubject)
//        .emailContent(modelSiignup.emailContent)
//        .build()
//}