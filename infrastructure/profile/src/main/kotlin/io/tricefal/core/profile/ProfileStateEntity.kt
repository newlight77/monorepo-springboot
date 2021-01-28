package io.tricefal.core.profile

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "profile_state")
class ProfileStateEntity(
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

    @Column(name = "portrait_uploaded")
    var portraitUploaded: Boolean? = null,

    @Column(name = "resume_uploaded")
    var resumeUploaded: Boolean? = null,

    @Column(name = "resumelinkedin_uploaded")
    var resumeLinkedinUploaded: Boolean? = null,

    @Column(name = "status_updated")
    var statusUpdated: Boolean? = null,

    @Column(name = "validated")
    var validated: Boolean? = null,

    @Column(name = "entreprise_form_filled")
    var entrepriseFormFilled: Boolean? = null,

    @Column(name = "mission_form_filled")
    var missionFormFilled: Boolean? = null,

    @Column(name = "completed")
    var completed: Boolean? = null,

    @Column(name = "deleted")
    val deleted: Boolean? = null
)


fun toEntity(domain: ProfileStateDomain): ProfileStateEntity {
    return ProfileStateEntity(
        id = null,
        username = domain.username,
        saved = domain.saved,
        registered = domain.registered,
        emailSent = domain.emailSent,
        emailValidated = domain.emailValidated,
        smsSent = domain.smsSent,
        smsValidated = domain.smsValidated,
        portraitUploaded = domain.portraitUploaded,
        resumeUploaded = domain.resumeUploaded,
        resumeLinkedinUploaded = domain.resumeLinkedinUploaded,
        statusUpdated = domain.statusUpdated,
        validated = domain.validated,
        entrepriseFormFilled = domain.entrepriseFormFilled,
        missionFormFilled = domain.missionFormFilled,
        completed = domain.completed,
        deleted = domain.deleted
    )
}

fun fromEntity(entity: ProfileStateEntity): ProfileStateDomain {
    return ProfileStateDomain.Builder(entity.username)
        .saved(entity.saved)
        .registered(entity.registered)
        .emailSent(entity.emailSent)
        .emailValidated(entity.emailValidated)
        .smsSent(entity.smsSent)
        .smsValidated(entity.smsValidated)
        .portraitUploaded(entity.portraitUploaded)
        .resumeUploaded(entity.resumeUploaded)
        .resumeLinkedinUploaded(entity.resumeLinkedinUploaded)
        .statusUpdated(entity.statusUpdated)
        .validated(entity.validated)
        .entrepriseFormFilled( entity.entrepriseFormFilled)
        .missionFormFilled(entity.missionFormFilled)
        .completed(entity.completed)
        .deleted(entity.deleted)
        .build()
}