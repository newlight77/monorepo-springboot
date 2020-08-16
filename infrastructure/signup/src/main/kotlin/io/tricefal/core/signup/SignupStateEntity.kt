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

            @Column(name = "okta_registered")
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
            var validated: Boolean?)


fun toEntity(domain: SignupStateDomain): SignupStateEntity {
    return SignupStateEntity(
            null,
            domain.username,
            domain.registered,
            domain.emailSent,
            domain.emailValidated,
            domain.activationCodeSent,
            domain.activatedByCode,
            domain.resumeUploaded,
            domain.statusUpdated,
            domain.validated
    )
}

fun fromEntity(entity: SignupStateEntity): SignupStateDomain {
    return SignupStateDomain.Builder(entity.username)
            .registered(entity.registered)
            .emailSent(entity.emailSent)
            .emailValidated(entity.emailValidated)
            .activationCodeSent(entity.activationCodeSent)
            .activatedByCode(entity.activatedByCode)
            .resumeUploaded(entity.resumeUploaded)
            .statusUpdated(entity.statusUpdated)
            .validated(entity.validated)
            .build()

}