package io.tricefal.core.profile

class ProfileStateDomain
    private constructor(
            val username: String,
            var saved: Boolean?,
            var registered: Boolean?,
            var cguAccepted: Boolean?,
            var emailSent: Boolean?,
            var emailValidated: Boolean? = null,
            var smsSent: Boolean?,
            var smsValidated: Boolean? = null,
            var portraitUploaded: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var resumeLinkedinUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var entrepriseFormFilled: Boolean? = null,
            var missionFormFilled: Boolean? = null,
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
            var smsSent: Boolean? = null,
            var smsValidated: Boolean? = null,
            var portraitUploaded: Boolean? = null,
            var resumeUploaded: Boolean? = null,
            var resumeLinkedinUploaded: Boolean? = null,
            var statusUpdated: Boolean? = null,
            var validated: Boolean? = null,
            var entrepriseFormFilled: Boolean? = null,
            var missionFormFilled: Boolean? = null,
            var completed: Boolean? = null,
            var deleted: Boolean? = null
    ) {
        fun saved(saved: Boolean?) = apply { this.saved = saved }
        fun registered(registered: Boolean?) = apply { this.registered = registered }
        fun cguAccepted(cguAccepted: Boolean?) = apply { this.cguAccepted = cguAccepted }
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
        fun deleted(deleted: Boolean?) = apply { this.deleted = deleted }
        fun build() = ProfileStateDomain(
            username = username,
            saved = saved,
            registered = registered,
            cguAccepted = cguAccepted,
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
            completed = completed,
            deleted = deleted
        )
    }

    fun updateState(state: String) {
        when(ProfileState.valueOf(state.toUpperCase())) {
            ProfileState.CGU_ACCEPTED -> this.cguAccepted = true
            ProfileState.REGISTERED -> this.registered = true
            ProfileState.SMS_CODE_SENT -> this.smsSent = true
            ProfileState.SMS_CODE_VALIDATED -> this.smsValidated = true
            ProfileState.EMAIL_SENT -> this.emailSent = true
            ProfileState.EMAIL_VALIDATED -> this.emailValidated = true
            ProfileState.PORTRAIT_UPLOADED -> this.portraitUploaded = true
            ProfileState.RESUME_UPLOADED -> this.resumeUploaded = true
            ProfileState.RESUME_LINKEDIN_UPLOADED -> this.resumeLinkedinUploaded = true
            ProfileState.STATUS_SET -> this.statusUpdated = true
            ProfileState.VALIDATED -> this.validated = true
            ProfileState.UNVALIDATED -> this.validated = false
            ProfileState.MISSION_FORM_FILLED -> this.missionFormFilled = true
            ProfileState.ENTERPRISE_FORM_FILLED -> this.entrepriseFormFilled = true
            ProfileState.COMPLETED -> this.completed = true
            ProfileState.DELETED -> this.deleted = true
        }
    }
}

enum class ProfileState {
    NONE,
    CGU_ACCEPTED,
    REGISTERED,
    SMS_CODE_SENT,
    SMS_CODE_VALIDATED,
    EMAIL_SENT,
    EMAIL_VALIDATED,
    PORTRAIT_UPLOADED, // optional
    RESUME_UPLOADED,
    RESUME_LINKEDIN_UPLOADED,
    STATUS_SET,
    VALIDATED, // activated by backoffice
    UNVALIDATED,
    MISSION_FORM_FILLED,
    ENTERPRISE_FORM_FILLED,
    COMPLETED, // everything is done
    DELETED; // soft deletion
}

