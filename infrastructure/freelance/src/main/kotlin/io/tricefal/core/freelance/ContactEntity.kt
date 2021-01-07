package io.tricefal.core.freelance

import java.time.Instant
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
        val email: @Size(min = 5, max = 100) String? = null,

        @Column(name = "lastName", length = 50)
        val lastName: @Size(max = 50) String? = null,

        @Column(name = "firstName", length = 50)
        val firstName: @Size(max = 50) String? = null,

        @Size(min = 2, max = 50)
        @Column(name = "lang_key", length = 50)
        var langKey: String? = null,

        @Column(name = "phone", length = 50)
        val phone: @Size(max = 22) String? = null,

        @Column(name = "landline", length = 50)
        val landline: @Size(max = 20) String? = null,

        @Column(name = "fax", length = 50)
        val fax: @Size(max = 20) String? = null,

        @Pattern(regexp = EMAIL_REGEX)
        @Column(length = 100)
        val email2: @Size(min = 5, max = 100) String ? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "address")
        var address: AddressEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

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
        address = domain.address?.let { toEntity(it) },
        lastDate = domain.lastDate
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
                address = entity.address?.let { fromEntity(it) },
                lastDate = entity.lastDate
        )
}
