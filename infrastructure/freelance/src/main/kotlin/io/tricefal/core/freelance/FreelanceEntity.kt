package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

@Entity
@Table(name = "freelance")
data class FreelanceEntity (
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @OneToOne
        @JoinColumn(name = "contact_id")
        var contact: ContactEntity? = null,

        @OneToOne
        @JoinColumn(name = "company_id")
        var company: CompanyEntity? = null,

        @OneToOne
        @JoinColumn(name = "privacy_detail_id")
        var privacyDetail: PrivacyDetailEntity? = null,

        @Column(name = "status", length = 50)
        var status: String?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "state")
        var state: FreelanceStateEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = null

)

fun toEntity(domain: FreelanceDomain): FreelanceEntity {
        return FreelanceEntity(
                null,
                domain.username,
                domain.contact?.let { toEntity(it) },
                domain.company?.let { toEntity(it) },
                domain.privacyDetail?.let { toEntity(it) },
                domain.status?.toString(),
                domain.state?.let { toEntity(it) }
        )
}

fun fromEntity(entity: FreelanceEntity): FreelanceDomain {
        return FreelanceDomain.Builder(entity.username)
                .contact(entity.contact?.let { fromEntity(it) })
                .company(entity.company?.let { fromEntity(it) })
                .privacyDetail(entity.privacyDetail?.let { fromEntity(it) })
                .status(entity.status?.toUpperCase()?.let { Status.valueOf(it) })
                .state(entity.state?.let { fromEntity(it) })
                .lastDate(entity.lastDate)
                .build()
}
