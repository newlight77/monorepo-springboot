package io.tricefal.core.signup

class SignupStateModel(
        val username: String,
        var saved: Boolean? = null,
        var registered: Boolean? = null,
        var emailSent: Boolean? = null,
        var emailValidated: Boolean? = null,
        var activationCodeSent: Boolean? = null,
        var activatedByCode: Boolean? = null,
        var resumeUploaded: Boolean? = null,
        var statusUpdated: Boolean? = null,
        var validated: Boolean? = null,
        val completed: Boolean? = null) {

    data class Builder (
            val username: String,
            var saved: Boolean? = null,
            var registered: Boolean? = null,
            var emailSent: Boolean? = null,
            var emailValidated: Boolean? = null,
            var activationCodeSent: Boolean? = null,
            var activatedByCode: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var completed: Boolean? = null) {
        fun saved(saved: Boolean?) = apply { this.saved = saved }
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun activationCodeSent(activationCodeSent: Boolean?) = apply { this.activationCodeSent = activationCodeSent }
        fun activatedByCode(activatedByCode: Boolean?) = apply { this.activatedByCode = activatedByCode }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun completed(completed: Boolean?) = apply { this.completed = completed }

        fun build() = SignupStateModel(
                username = username,
                saved = saved,
                registered = registered,
                emailSent = emailSent,
                emailValidated = emailValidated,
                activationCodeSent = activationCodeSent,
                activatedByCode = activatedByCode,
                resumeUploaded = resumeUploaded,
                statusUpdated = statusUpdated,
                validated = validated,
                completed = completed
        )
    }
}

fun toModel(domain: SignupStateDomain): SignupStateModel {
    return SignupStateModel.Builder(domain.username)
            .saved(domain.saved)
            .registered(domain.registered)
            .emailSent(domain.emailSent)
            .emailValidated(domain.emailValidated)
            .activationCodeSent(domain.smsSent)
            .activatedByCode(domain.smsValidated)
            .resumeUploaded(domain.resumeUploaded)
            .statusUpdated(domain.statusUpdated)
            .validated(domain.validated)
            .completed(domain.completed)
            .build()
}

fun fromModel(model: SignupStateModel): SignupStateDomain {
    return SignupStateDomain.Builder(model.username)
            .saved(model.saved)
            .registered(model.registered)
            .emailSent(model.emailSent)
            .emailValidated(model.emailValidated)
            .smsSent(model.activationCodeSent)
            .smsValidated(model.activatedByCode)
            .resumeUploaded(model.resumeUploaded)
            .statusUpdated(model.statusUpdated)
            .validated(model.validated)
            .completed(model.completed)
            .build()
}