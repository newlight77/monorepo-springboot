package io.tricefal.core.freelance

import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "privacy_detail")
data class PrivacyDetailEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long?,

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
        val information: String? = null

)

fun toEntity(domain: PrivacyDetailDomain) = PrivacyDetailEntity(
        null,
        domain.username,
        domain.birthDate,
        domain.birthCity,
        domain.birthCountry,
        domain.citizenship,
        domain.socialSecurityNumber,
        domain.nationalIdentityNumber,
        domain.information
)

fun fromEntity(entity: PrivacyDetailEntity) : PrivacyDetailDomain {
        return PrivacyDetailDomain(
                entity.username,
                entity.birthDate,
                entity.birthCity,
                entity.birthCountry,
                entity.citizenship,
                entity.socialSecurityNumber,
                entity.nationalIdentityNumber,
                entity.information
        )
}
