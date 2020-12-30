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

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "contact_id")
        var contact: ContactEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "company_id")
        var company: CompanyEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "privacy_detail_id")
        var privacyDetail: PrivacyDetailEntity? = null,

        @Column(name = "status", length = 50)
        var status: String? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "state")
        var state: FreelanceStateEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: FreelanceDomain): FreelanceEntity {
        return FreelanceEntity(
                id = null,
                username = domain.username,
                contact = domain.contact?.let { toEntity(it) },
                company = domain.company?.let { toEntity(it) },
                privacyDetail = domain.privacyDetail?.let { toEntity(it) },
                status = domain.status?.toString(),
                state = domain.state?.let { toEntity(it) }
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
