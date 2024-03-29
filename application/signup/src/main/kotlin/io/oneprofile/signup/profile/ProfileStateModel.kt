package io.oneprofile.signup.profile

class ProfileStateModel(
    val username: String,
    var saved: Boolean? = null,
    var registered: Boolean? =null,
    var cguAccepted: Boolean? = null,
    var emailSent: Boolean? = null,
    var emailValidated: Boolean? = null,
    var smsSent: Boolean? = null,
    var smsValidated: Boolean? = null,
    var portraitUploaded: Boolean? = null,
    var resumeUploaded: Boolean? = null,
    var resumeLinkedinUploaded: Boolean? = null,
    var statusUpdated: Boolean? = null,
    var validated: Boolean? = null,
    var entrepriseFormFilled: Boolean? = null,
    var missionFormFilled: Boolean? = null,
    val completed: Boolean? = null
) {

    data class Builder (
        val username: String,
        var saved: Boolean? = null,
        var cguAccepted: Boolean? = null,
        var registered: Boolean? = null,
        var emailSent: Boolean? = null,
        var emailValidated: Boolean? = null,
        var smsSent: Boolean? = null,
        var smsValidated: Boolean? = null,
        var portraitUploaded: Boolean? = null,
        var resumeUploaded: Boolean? = null,
        var resumeLinkedinUploaded: Boolean? = null,
        var statusUpdated: Boolean? = null,
        var validated: Boolean? = null,
        var entrepriseFormFilled: Boolean? = null,
        var missionFormFilled: Boolean? = null,
        var completed: Boolean? = null
) {
        fun saved(saved: Boolean?) = apply { this.saved = saved }
        fun cguAccepted(cguAccepted: Boolean?) = apply { this.cguAccepted = cguAccepted }
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun smsSent(smsSent: Boolean?) = apply { this.smsSent = smsSent }
        fun smsValidated(smsValidated: Boolean?) = apply { this.smsValidated = smsValidated }
        fun portraitUploaded(portraitUploaded: Boolean?) = apply { this.portraitUploaded = portraitUploaded }
        fun resumeUploaded(resumeUploaded: Boolean?) = apply { this.resumeUploaded = resumeUploaded }
        fun resumeLinkedinUploaded(resumeLinkedinUploaded: Boolean?) = apply { this.resumeLinkedinUploaded = resumeLinkedinUploaded }
        fun statusUpdated(statusUpdated: Boolean?) = apply { this.statusUpdated = statusUpdated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun entrepriseFormFilled(entrepriseFormFilled: Boolean?) = apply { this.entrepriseFormFilled = entrepriseFormFilled }
        fun missionFormFilled(missionFormFilled: Boolean?) = apply { this.missionFormFilled = missionFormFilled }
        fun completed(completed: Boolean?) = apply { this.completed = completed }

        fun build() = ProfileStateModel(
            username = username,
            saved = saved,
            cguAccepted = cguAccepted,
            registered = registered,
            emailSent = emailSent,
            emailValidated = emailValidated,
            smsSent = smsSent,
            smsValidated = smsValidated,
            portraitUploaded = portraitUploaded,
            resumeUploaded = resumeUploaded,
            resumeLinkedinUploaded = resumeLinkedinUploaded,
            statusUpdated = statusUpdated,
            validated = validated,
            entrepriseFormFilled = entrepriseFormFilled,
            missionFormFilled = missionFormFilled,
            completed = completed
        )
    }
}

fun toModel(domain: ProfileStateDomain): ProfileStateModel {
    return ProfileStateModel.Builder(domain.username)
        .saved(domain.saved)
        .cguAccepted(domain.cguAccepted)
        .registered(domain.registered)
        .emailSent(domain.emailSent)
        .emailValidated(domain.emailValidated)
        .smsSent(domain.smsSent)
        .smsValidated(domain.smsValidated)
        .portraitUploaded(domain.portraitUploaded)
        .resumeUploaded(domain.resumeUploaded)
        .resumeLinkedinUploaded(domain.resumeLinkedinUploaded)
        .statusUpdated(domain.statusUpdated)
        .validated(domain.validated)
        .entrepriseFormFilled(domain.entrepriseFormFilled)
        .missionFormFilled(domain.missionFormFilled)
        .completed(domain.completed)
        .build()
}

fun fromModel(model: ProfileStateModel): ProfileStateDomain {
    return ProfileStateDomain.Builder(model.username)
        .saved(model.saved)
        .cguAccepted(model.cguAccepted)
        .registered(model.registered)
        .emailSent(model.emailSent)
        .emailValidated(model.emailValidated)
        .smsSent(model.smsSent)
        .smsValidated(model.smsValidated)
        .portraitUploaded(model.portraitUploaded)
        .resumeUploaded(model.resumeUploaded)
        .resumeLinkedinUploaded(model.resumeLinkedinUploaded)
        .statusUpdated(model.statusUpdated)
        .validated(model.validated)
        .entrepriseFormFilled(model.entrepriseFormFilled)
        .missionFormFilled(model.missionFormFilled)
        .completed(model.completed)
        .build()
}