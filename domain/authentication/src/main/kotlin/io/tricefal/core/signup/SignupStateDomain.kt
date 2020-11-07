package io.tricefal.core.signup

class SignupStateDomain
    private constructor(
            val username: String,
            var saved: Boolean?,
            var registered: Boolean?,
            var cguAccepted: Boolean?,
            var emailSent: Boolean?,
            var emailValidated: Boolean? = null,
            var activationCodeSent: Boolean?,
            var activatedByCode: Boolean? = null,
            var portraitUploaded: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var resumeLinkedinUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var completed: Boolean? = null,
            var deleted: Boolean?
    ) {



    data class Builder (
            val username: String,
            var saved: Boolean? = null,
            var registered: Boolean? = null,
            var cguAccepted: Boolean? = null,
            var emailSent: Boolean? = null,
            var emailValidated: Boolean? = null,
            var activationCodeSent: Boolean? = null,
            var activatedByCode: Boolean? = null,
            var portraitUploaded: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var resumeLinkedinUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var completed: Boolean? = null,
            var deleted: Boolean? = null
    ) {
        fun saved(saved: Boolean?) = apply { this.saved = saved }
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun cguAccepted(cguAccepted: Boolean?) = apply { this.cguAccepted = cguAccepted }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
//        fun emailSent(isEmailSent: () -> Boolean) = apply { this.emailSent = isEmailSent.invoke() }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun activationCodeSent(activationCodeSent: Boolean?) = apply { this.activationCodeSent = activationCodeSent }
//        fun activationCodeSent(isActivationCodeSent: () -> Boolean) = apply { this.activationCodeSent = isActivationCodeSent.invoke() }
        fun activatedByCode(activatedByCode: Boolean?) = apply { this.activatedByCode = activatedByCode }
        fun portraitUploaded(portraitUploaded: Boolean?) = apply { this.portraitUploaded = portraitUploaded }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun resumeLinkedinUploaded(resumeLinkedinUploaded: Boolean?) = apply { this.resumeLinkedinUploaded = resumeLinkedinUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun completed(completed: Boolean?) = apply { this.completed = completed }
        fun deleted(deleted: Boolean?) = apply { this.deleted = deleted }
        fun build() = SignupStateDomain(
                username = username,
                saved = saved,
                registered = registered,
                cguAccepted = cguAccepted,
                emailSent = emailSent,
                emailValidated = emailValidated,
                activationCodeSent = activationCodeSent,
                activatedByCode = activatedByCode,
                portraitUploaded = portraitUploaded,
                resumeUploaded = resumeUploaded,
                resumeLinkedinUploaded = resumeLinkedinUploaded,
                statusUpdated = statusUpdated,
                validated = validated,
                completed = completed,
                deleted = deleted
        )
    }
}
