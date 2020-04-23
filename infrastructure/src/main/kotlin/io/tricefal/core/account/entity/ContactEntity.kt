package io.tricefal.core.account.entity

import io.tricefal.core.account.domain.ContactDomain
import io.tricefal.core.login.domain.EMAIL_REGEX
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "contact")
data class ContactEntity(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        var id: Long,

        @Column(name = "nom", length = 50)
        val lastName: @Size(max = 50) String,

        @Column(name = "prenom", length = 50)
        val firstName: @Size(max = 50) String,

        @Size(min = 2, max = 6)
        @Column(name = "lang_key", length = 6)
        var langKey: String,

        @Column(name = "image_url", length = 256)
        var imageUrl: @Size(max = 256) String,

        @OneToOne
        @JoinColumn(name = "address")
        val address: AddressEntity,

        @Column(name = "phone", length = 50)
        val phone: @Size(max = 50) String,

        @Column(name = "fax", length = 50)
        val fax: @Size(max = 50) String,

        @Column(name = "landline", length = 50)
        val landline: String,

        @Pattern(regexp = EMAIL_REGEX)
        @Column(length = 100, unique = true)
        val email: @Size(min = 5, max = 100) String,

        @Pattern(regexp = EMAIL_REGEX)
        @Column(length = 100, unique = true)
        val email2: @Size(min = 5, max = 100) String
)

fun fromDomain(domain: ContactDomain) = ContactEntity(
        domain.id,
        domain.lastName,
        domain.firstName,
        domain.langKey,
        domain.imageUrl,
        fromDomain(domain.address),
        domain.phone,
        domain.fax,
        domain.landline,
        domain.email,
        domain.email2
)

fun toDomain(entity: ContactEntity): ContactDomain {
        return ContactDomain(
                entity.id,
                entity.lastName,
                entity.firstName,
                entity.langKey,
                entity.imageUrl,
                toDomain(entity.address),
                entity.phone,
                entity.fax,
                entity.landline,
                entity.email,
                entity.email2
        )
}
