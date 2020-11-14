package io.tricefal.core.freelance

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "freelance_state")
class FreelanceStateEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        val username: String,

        @Column(name = "kbis_uploaded")
        var kbisUploaded: Boolean? = null,
        @Column(name = "rib_uploaded")
        var ribUploaded: Boolean? = null,
        @Column(name = "rc_uploaded")
        var rcUploaded: Boolean? = null,
        @Column(name = "urssaf_uploaded")
        var urssafUploaded: Boolean? = null,
        @Column(name = "fiscal_uploaded")
        var fiscalUploaded: Boolean? = null,
        @Column(name = "cvmission_uploaded")
        var cvMissionUploaded: Boolean? = null,
        @Column(name = "completed")
        var completed: Boolean? = null
)

fun toEntity(domain: FreelanceStateDomain): FreelanceStateEntity {
    return FreelanceStateEntity(
            null,
            domain.username,
            domain.kbisUploaded,
            domain.ribUploaded,
            domain.rcUploaded,
            domain.urssafUploaded,
            domain.fiscalUploaded,
            domain.missionResumedUploaded,
            domain.completed)
}

fun fromEntity(entity: FreelanceStateEntity): FreelanceStateDomain {
    return FreelanceStateDomain.Builder(entity.username)
            .kbisUploaded(entity.kbisUploaded)
            .ribUploaded(entity.ribUploaded)
            .rcUploaded(entity.rcUploaded)
            .urssafUploaded(entity.urssafUploaded)
            .fiscalUploaded(entity.fiscalUploaded)
            .missionResumedUploaded(entity.cvMissionUploaded)
            .completed(entity.completed)
            .build()
}