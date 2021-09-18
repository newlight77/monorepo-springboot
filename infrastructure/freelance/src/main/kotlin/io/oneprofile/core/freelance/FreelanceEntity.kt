package io.oneprofile.core.freelance

import io.oneprofile.core.company.AddressEntity
import io.oneprofile.core.company.CompanyEntity
import io.oneprofile.core.company.ContactEntity
import io.oneprofile.core.company.PrivacyDetailEntity
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
        @JoinColumn(name = "address_id")
        var address: AddressEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "company_id")
        var company: CompanyEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "privacy_detail_id")
        var privacyDetail: PrivacyDetailEntity? = null,

        @Column(name = "with_mission")
        var withMission: Boolean? = null,

        @Column(name = "availability", length = 50)
        var availability: String? = null,

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
                contact = domain.contact?.let { io.oneprofile.core.company.toEntity(it) },
                address = domain.address?.let { io.oneprofile.core.company.toEntity(it) },
                company = domain.company?.let { io.oneprofile.core.company.toEntity(it) },
                privacyDetail = domain.privacyDetail?.let { io.oneprofile.core.company.toEntity(it) },
                withMission = domain.withMission,
                availability = domain.availability?.toString(),
                state = domain.state?.let { toEntity(it) },
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: FreelanceEntity): FreelanceDomain {
        return FreelanceDomain.Builder(entity.username)
                .contact(entity.contact?.let { io.oneprofile.core.company.fromEntity(it) })
                .address(entity.address?.let { io.oneprofile.core.company.fromEntity(it) })
                .company(entity.company?.let { io.oneprofile.core.company.fromEntity(it) })
                .privacyDetail(entity.privacyDetail?.let { io.oneprofile.core.company.fromEntity(it) })
                .withMission(entity.withMission)
                .availability(entity.availability?.toUpperCase()?.let { Availability.valueOf(it) })
                .state(entity.state?.let { fromEntity(it) })
                .lastDate(entity.lastDate)
                .build()
}
