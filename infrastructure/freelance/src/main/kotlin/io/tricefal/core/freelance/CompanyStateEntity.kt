package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "company_state")
class CompanyStateEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 3, max = 100)
        @Column(name = "company_name", length = 50)
        val companyName: String,

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

        @Column(name = "completed")
        var completed: Boolean? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: CompanyStateDomain): CompanyStateEntity {
    return CompanyStateEntity(
            null,
            domain.companyName,
            domain.kbisUploaded,
            domain.ribUploaded,
            domain.rcUploaded,
            domain.urssafUploaded,
            domain.fiscalUploaded,
            domain.completed,
            domain.lastDate)
}

fun fromEntity(entity: CompanyStateEntity): CompanyStateDomain {
    return CompanyStateDomain.Builder(entity.companyName)
            .kbisUploaded(entity.kbisUploaded)
            .ribUploaded(entity.ribUploaded)
            .rcUploaded(entity.rcUploaded)
            .urssafUploaded(entity.urssafUploaded)
            .fiscalUploaded(entity.fiscalUploaded)
            .completed(entity.completed)
            .lastDate(entity.lastDate)
            .build()
}