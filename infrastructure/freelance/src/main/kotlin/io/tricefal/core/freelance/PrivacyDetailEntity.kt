package io.tricefal.core.freelance

import java.time.Instant
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "privacy_detail")
data class PrivacyDetailEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Column(name = "username")
        val username: String,

        @Column(name = "birth_date")
        val birthDate: LocalDate? = null,

        @Column(name = "birth_city")
        val birthCity: String? = null,

        @Column(name = "birth_country")
        val birthCountry: String? = null,

        @Column(name = "citizenship")
        val citizenship: String? = null,

        @Column(name = "ssn", length = 50)
        val socialSecurityNumber: @Size(max = 50) String? = null,

        @Column(name = "nin", length = 50)
        val nationalIdentityNumber: @Size(max = 50) String? = null,

        @Column(name = "information")
        val information: String? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: PrivacyDetailDomain) = PrivacyDetailEntity(
        id = null,
        username = domain.username,
        birthDate =  domain.birthDate,
        birthCity = domain.birthCity,
        birthCountry =  domain.birthCountry,
        citizenship = domain.citizenship,
        socialSecurityNumber = domain.socialSecurityNumber,
        nationalIdentityNumber = domain.nationalIdentityNumber,
        information = domain.information,
        lastDate = domain.lastDate
)

fun fromEntity(entity: PrivacyDetailEntity) : PrivacyDetailDomain {
        return PrivacyDetailDomain(
                username = entity.username,
                birthDate = entity.birthDate,
                birthCity = entity.birthCity,
                birthCountry =  entity.birthCountry,
                citizenship = entity.citizenship,
                socialSecurityNumber = entity.socialSecurityNumber,
                nationalIdentityNumber = entity.nationalIdentityNumber,
                information = entity.information,
                lastDate = entity.lastDate
        )
}
