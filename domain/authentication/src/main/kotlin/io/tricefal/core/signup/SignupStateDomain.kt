package io.tricefal.core.signup

class SignupStateDomain
    private constructor(
        val username: String,
        val registered: Boolean?,
        val emailSent: Boolean?,
        var emailValidated: Boolean? = null,
        val activationCodeSent: Boolean?,
        var activatedByCode: Boolean? = null,
        var portraitUploaded: Boolean? = null,
        var resumeUploaded: Boolean? = null,
        var refUploaded: Boolean? = null,
        var statusUpdated: Boolean? = null,
        var validated: Boolean? = null,
        var completed: Boolean? = null) {



    data class Builder (
            val username: String,
            var registered: Boolean? = null,
            var emailSent: Boolean? = null,
            var emailValidated: Boolean? = null,
            var activationCodeSent: Boolean? = null,
            var activatedByCode: Boolean? = null,
            var portraitUploaded: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var refUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var completed: Boolean? = null) {
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
//        fun emailSent(isEmailSent: () -> Boolean) = apply { this.emailSent = isEmailSent.invoke() }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun activationCodeSent(activationCodeSent: Boolean?) = apply { this.activationCodeSent = activationCodeSent }
//        fun activationCodeSent(isActivationCodeSent: () -> Boolean) = apply { this.activationCodeSent = isActivationCodeSent.invoke() }
        fun activatedByCode(activatedByCode: Boolean?) = apply { this.activatedByCode = activatedByCode }
        fun portraitUploaded(portraitUploaded: Boolean?) = apply { this.portraitUploaded = portraitUploaded }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun refUploaded(refUploaded: Boolean?) = apply { this.refUploaded = refUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun completed(completed: Boolean?) = apply { this.completed = completed }
        fun build() = SignupStateDomain(
                username,
                registered,
                emailSent,
                emailValidated,
                activationCodeSent,
                activatedByCode,
                portraitUploaded,
                resumeUploaded,
                refUploaded,
                statusUpdated,
                validated,
                completed)
    }
}
