package io.oneprofile.signup.signup

class SignupStateModel(
        val username: String,
        var saved: Boolean? = null,
        var registered: Boolean? =null,
        var cguAccepted: Boolean? = null,
        var emailSent: Boolean? = null,
        var emailValidated: Boolean? = null,
        var smsSent: Boolean? = null,
        var smsValidated: Boolean? = null,
        var resumeUploaded: Boolean? = null,
        var resumeLinkedinUploaded: Boolean? = null,
        var statusUpdated: Boolean? = null,
        var validated: Boolean? = null,
        val completed: Boolean? = null) {

    data class Builder (
            val username: String,
            var saved: Boolean? = null,
            var cguAccepted: Boolean? = null,
            var registered: Boolean? = null,
            var emailSent: Boolean? = null,
            var emailValidated: Boolean? = null,
            var smsSent: Boolean? = null,
            var smsValidated: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var resumeLinkedinUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var completed: Boolean? = null
) {
        fun saved(saved: Boolean?) = apply { this.saved = saved }
        fun cguAccepted(cguAccepted: Boolean?) = apply { this.cguAccepted = cguAccepted }
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun smsSent(smsSent: Boolean?) = apply { this.smsSent = smsSent }
        fun smsValidated(smsValidated: Boolean?) = apply { this.smsValidated = smsValidated }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun resumeLinkedinUploaded(resumeLinkedinUploaded: Boolean?) = apply { this.resumeLinkedinUploaded = resumeLinkedinUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun completed(completed: Boolean?) = apply { this.completed = completed }

        fun build() = SignupStateModel(
                username = username,
                saved = saved,
                cguAccepted = cguAccepted,
                registered = registered,
                emailSent = emailSent,
                emailValidated = emailValidated,
                smsSent = smsSent,
                smsValidated = smsValidated,
                resumeUploaded = resumeUploaded,
                resumeLinkedinUploaded = resumeLinkedinUploaded,
                statusUpdated = statusUpdated,
                validated = validated,
                completed = completed
        )
    }
}

fun toModel(domain: SignupStateDomain): SignupStateModel {
    return SignupStateModel.Builder(domain.username)
            .saved(domain.saved)
            .cguAccepted(domain.cguAccepted)
            .registered(domain.registered)
            .emailSent(domain.emailSent)
            .emailValidated(domain.emailValidated)
            .smsSent(domain.smsSent)
            .smsValidated(domain.smsValidated)
            .resumeUploaded(domain.resumeUploaded)
            .resumeLinkedinUploaded(domain.resumeLinkedinUploaded)
            .statusUpdated(domain.statusUpdated)
            .validated(domain.validated)
            .completed(domain.completed)
            .build()
}

fun fromModel(model: SignupStateModel): SignupStateDomain {
    return SignupStateDomain.Builder(model.username)
            .saved(model.saved)
            .cguAccepted(model.cguAccepted)
            .registered(model.registered)
            .emailSent(model.emailSent)
            .emailValidated(model.emailValidated)
            .smsSent(model.smsSent)
            .smsValidated(model.smsValidated)
            .resumeUploaded(model.resumeUploaded)
            .resumeLinkedinUploaded(model.resumeLinkedinUploaded)
            .statusUpdated(model.statusUpdated)
            .validated(model.validated)
            .completed(model.completed)
            .build()
}