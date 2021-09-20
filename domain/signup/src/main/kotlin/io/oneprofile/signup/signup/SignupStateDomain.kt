package io.oneprofile.signup.signup

class SignupStateDomain
    private constructor(
            val username: String,
            var saved: Boolean?,
            var registered: Boolean?,
            var cguAccepted: Boolean?,
            var emailSent: Boolean?,
            var emailValidated: Boolean? = null,
            var smsSent: Boolean?,
            var smsValidated: Boolean? = null,
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
            var smsSent: Boolean? = null,
            var smsValidated: Boolean? = null,
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
        fun emailValidated(emailValidated: Boolean?) = apply { this.emailValidated = emailValidated }
        fun smsSent(smsSent: Boolean?) = apply { this.smsSent = smsSent }
        fun smsValidated(smsValidated: Boolean?) = apply { this.smsValidated = smsValidated }
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
                smsSent = smsSent,
                smsValidated = smsValidated,
                resumeUploaded = resumeUploaded,
                resumeLinkedinUploaded = resumeLinkedinUploaded,
                statusUpdated = statusUpdated,
                validated = validated,
                completed = completed,
                deleted = deleted
        )
    }
}
