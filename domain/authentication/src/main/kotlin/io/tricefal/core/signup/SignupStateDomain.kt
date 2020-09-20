package io.tricefal.core.signup

class SignupStateDomain
    private constructor(
        val username: String,
        val registered: Boolean?,
        val emailSent: Boolean?,
        var emailValidated: Boolean? = null,
        val activationCodeSent: Boolean?,
        var activatedByCode: Boolean? = null,
        var resumeUploaded: Boolean? = null,
        var statusUpdated: Boolean? = null,
        var validated: Boolean?) {


    data class Builder (
            val username: String,
            var registered: Boolean? = null,
            var emailSent: Boolean? = null,
            var emailValidated: Boolean? = null,
            var activationCodeSent: Boolean? = null,
            var activatedByCode: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null) {
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun activationCodeSent(activationCodeSent: Boolean?) = apply { this.activationCodeSent = activationCodeSent }
        fun activatedByCode(activatedByCode: Boolean?) = apply { this.activatedByCode = activatedByCode }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun build() = SignupStateDomain(
                username,
                registered,
                emailSent,
                emailValidated,
                activationCodeSent,
                activatedByCode,
                resumeUploaded,
                statusUpdated,
                validated)
    }
}