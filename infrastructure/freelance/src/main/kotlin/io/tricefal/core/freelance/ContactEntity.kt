package io.tricefal.core.freelance

import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "contact")
data class ContactEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Pattern(regexp = EMAIL_REGEX)
        @Column(length = 100, unique = true)
        val email: @Size(min = 5, max = 100) String,

        @Column(name = "lastName", length = 50)
        val lastName: @Size(max = 50) String? = null,

        @Column(name = "firstName", length = 50)
        val firstName: @Size(max = 50) String? = null,

        @Size(min = 2, max = 6)
        @Column(name = "lang_key", length = 6)
        var langKey: String? = null,

        @Column(name = "phone", length = 50)
        val phone: @Size(max = 50) String? = null,

        @Column(name = "landline", length = 50)
        val landline: String? = null,

        @Column(name = "fax", length = 50)
        val fax: @Size(max = 50) String? = null,

        @Pattern(regexp = EMAIL_REGEX)
        @Column(length = 100)
        val email2: @Size(min = 5, max = 100) String ? = null,

        @OneToOne
        @JoinColumn(name = "address")
        val address: AddressEntity? = null
)

fun toEntity(domain: ContactDomain) = ContactEntity(
        id = null,
        email = domain.email,
        lastName = domain.lastName,
        firstName = domain.firstName,
        langKey = domain.langKey,
        phone = domain.phone,
        landline = domain.landline,
        fax = domain.fax,
        email2 = domain.email2,
        address = domain.address?.let { toEntity(it) }
)

fun fromEntity(entity: ContactEntity): ContactDomain {
        return ContactDomain(
                email = entity.email,
                lastName = entity.lastName,
                firstName = entity.firstName,
                langKey =  entity.langKey,
                phone = entity.phone,
                landline = entity.landline,
                fax = entity.fax,
                email2 = entity.email2,
                address = entity.address?.let { fromEntity(it) }
        )
}
