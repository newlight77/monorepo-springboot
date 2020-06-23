package io.tricefal.core.signup

class SignupStateModel
    private constructor(
        val username: String,
        var oktaRegistered: Boolean? = null,
        var emailSent: Boolean? = null,
        var emailValidated: Boolean? = null,
        var activationCodeSent: Boolean? = null,
        var activatedByCode: Boolean? = null,
        var resumeUploaded: Boolean? = null,
        var statusUpdated: Boolean? = null,
        val validated: Boolean?) {

    data class Builder (
            val username: String,
            var oktaRegistered: Boolean? = null,
            var emailSent: Boolean? = null,
            var emailValidated: Boolean? = null,
            var activationCodeSent: Boolean? = null,
            var activatedByCode: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null) {
        fun oktaRegistered(oktaRegistered: Boolean?) = apply { this.oktaRegistered = oktaRegistered }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun activationCodeSent(activationCodeSent: Boolean?) = apply { this.activationCodeSent = activationCodeSent }
        fun activatedByCode(activatedByCode: Boolean?) = apply { this.activatedByCode = activatedByCode }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }

        fun build() = SignupStateModel(
                username,
                oktaRegistered,
                emailSent,
                emailValidated,
                activationCodeSent,
                activatedByCode,
                resumeUploaded,
                statusUpdated,
                validated
        )
    }
}

fun toModel(domain: SignupStateDomain): SignupStateModel {
    return SignupStateModel.Builder(domain.username)
            .oktaRegistered(domain.oktaRegistered)
            .emailSent(domain.emailSent)
            .emailValidated(domain.emailValidated)
            .activationCodeSent(domain.activationCodeSent)
            .activatedByCode(domain.activatedByCode)
            .resumeUploaded(domain.resumeUploaded)
            .statusUpdated(domain.statusUpdated)
            .validated(domain.validated)
            .build()
}

fun fromModel(model: SignupStateModel): SignupStateDomain {
    return SignupStateDomain.Builder(model.username)
            .oktaRegistered(model.oktaRegistered)
            .emailSent(model.emailSent)
            .emailValidated(model.emailValidated)
            .activationCodeSent(model.activationCodeSent)
            .activatedByCode(model.activatedByCode)
            .resumeUploaded(model.resumeUploaded)
            .statusUpdated(model.statusUpdated)
            .validated(model.validated)
            .build()
}