package io.tricefal.core.signup

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "signup_state")
class SignupStateEntity(
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Id
            val id: Long? = null,

            @NotNull
            @Pattern(regexp = EMAIL_REGEX)
            @Size(min = 10, max = 50)
            @Column(name = "username", length = 50)
            var username: String,

            @Column(name = "saved")
            val saved: Boolean? = null,

            @Column(name = "registered")
            val registered: Boolean? = null,

            @Column(name = "email_sent")
            val emailSent: Boolean? = null,

            @Column(name = "email_validated")
            var emailValidated: Boolean? = null,

            @Column(name = "sms_sent")
            val smsSent: Boolean? = null,

            @Column(name = "sms_validated")
            var smsValidated: Boolean? = null,

            @Column(name = "resume_uploaded")
            var resumeUploaded: Boolean? = null,

            @Column(name = "status_updated")
            var statusUpdated: Boolean? = null,

            @Column(name = "validated")
            var validated: Boolean? = null,

            @Column(name = "completed")
            var completed: Boolean? = null,

            @Column(name = "deleted")
            val deleted: Boolean? = null
)


fun toEntity(domain: SignupStateDomain): SignupStateEntity {
    return SignupStateEntity(
            id = null,
            username = domain.username,
            saved = domain.saved,
            registered = domain.registered,
            emailSent = domain.emailSent,
            emailValidated = domain.emailValidated,
            smsSent = domain.smsSent,
            smsValidated = domain.smsValidated,
            resumeUploaded = domain.resumeUploaded,
            statusUpdated = domain.statusUpdated,
            validated = domain.validated,
            completed = domain.completed,
            deleted = domain.deleted
    )
}

fun fromEntity(entity: SignupStateEntity): SignupStateDomain {
    return SignupStateDomain.Builder(entity.username)
            .saved(entity.saved)
            .registered(entity.registered)
            .emailSent(entity.emailSent)
            .emailValidated(entity.emailValidated)
            .smsSent(entity.smsSent)
            .smsValidated(entity.smsValidated)
            .resumeUploaded(entity.resumeUploaded)
            .statusUpdated(entity.statusUpdated)
            .validated(entity.validated)
            .completed(entity.completed)
            .deleted(entity.deleted)
            .build()

}