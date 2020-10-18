package io.tricefal.core.freelance

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

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "state")
        var state: FreelanceStateEntity? = null
)

fun toEntity(domain: FreelanceDomain): FreelanceEntity {
        return FreelanceEntity(
                null,
                domain.username,
                domain.contact?.let { toEntity(it) },
                domain.company?.let { toEntity(it) },
                domain.privacyDetail?.let { toEntity(it) },
                domain.state?.let { toEntity(it) }
        )
}

fun fromEntity(entity: FreelanceEntity): FreelanceDomain {
        return FreelanceDomain.Builder(entity.username)
                .contact(entity.contact?.let { fromEntity(it) })
                .company(entity.company?.let { fromEntity(it) })
                .privacyDetail(entity.privacyDetail?.let { fromEntity(it) })
                .state(entity.state?.let { fromEntity(it) })
                .build()
}
