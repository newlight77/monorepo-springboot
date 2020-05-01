package io.tricefal.core.account.entity

import io.tricefal.core.account.domain.PrivacyDetailDomain
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "privacy_detail")
data class PrivacyDetailEntity(
//        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//        @SequenceGenerator(name = "sequenceGenerator")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long,

        @Column(name = "birth_date")
        val birthDate: LocalDate,

        @Column(name = "birth_city")
        val birthCity: String,

        @Column(name = "birth_country")
        val birthCountry: String,

        @Column(name = "citizenship")
        val citizenship: String,

        @Column(name = "social_security_number", length = 50)
        val socialSecurityNumber: @Size(max = 50) String,

        @Column(name = "information")
        val information: String,

        @Column(name = "cin_url", length = 256)
        val cinUrl: @Size(max = 256) String,

        @Column(name = "social_security_url", length = 256)
        val socialSecurityUrl: @Size(max = 256) String,

        @Column(name = "visa_url", length = 256)
        val visaUrl: @Size(max = 256) String,

        @Column(name = "diploma_url", length = 256)
        val diplomaUrl: @Size(max = 256) String,

        @Column(name = "casier_judiciaire_url", length = 256)
        val casierJudiciaireUrl: @Size(max = 256) String

)

fun fromDomain(domain: PrivacyDetailDomain) = PrivacyDetailEntity(
        domain.id,
        domain.birthDate,
        domain.birthCity,
        domain.birthCountry,
        domain.citizenship,
        domain.socialSecurityNumber,
        domain.information,
        domain.cinUrl,
        domain.socialSecurityUrl,
        domain.visaUrl,
        domain.diplomaUrl,
        domain.casierJudiciaireUrl
)

fun toDomain(entity: PrivacyDetailEntity) : PrivacyDetailDomain {
        return PrivacyDetailDomain(
                entity.id,
                entity.birthDate,
                entity.birthCity,
                entity.birthCountry,
                entity.citizenship,
                entity.socialSecurityNumber,
                entity.information,
                entity.cinUrl,
                entity.socialSecurityUrl,
                entity.visaUrl,
                entity.diplomaUrl,
                entity.casierJudiciaireUrl
        )
}
