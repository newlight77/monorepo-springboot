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
            val saved: Boolean?,

            @Column(name = "registered")
            val registered: Boolean?,

            @Column(name = "email_sent")
            val emailSent: Boolean?,

            @Column(name = "email_validated")
            var emailValidated: Boolean? = null,

            @Column(name = "activation_code_Sent")
            val activationCodeSent: Boolean?,

            @Column(name = "activated_by_Code")
            var activatedByCode: Boolean? = null,

            @Column(name = "resume_uploaded")
            var resumeUploaded: Boolean? = null,

            @Column(name = "status_updated")
            var statusUpdated: Boolean? = null,

            @Column(name = "validated")
            var validated: Boolean? = null,

            @Column(name = "completed")
            var completed: Boolean? = null,

            @Column(name = "deleted")
            val deleted: Boolean?
)


fun toEntity(domain: SignupStateDomain): SignupStateEntity {
    return SignupStateEntity(
            id = null,
            username = domain.username,
            saved = domain.saved,
            registered = domain.registered,
            emailSent = domain.emailSent,
            emailValidated = domain.emailValidated,
            activationCodeSent = domain.activationCodeSent,
            activatedByCode = domain.activatedByCode,
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
            .activationCodeSent(entity.activationCodeSent)
            .activatedByCode(entity.activatedByCode)
            .resumeUploaded(entity.resumeUploaded)
            .statusUpdated(entity.statusUpdated)
            .validated(entity.validated)
            .completed(entity.completed)
            .deleted(entity.deleted)
            .build()

}