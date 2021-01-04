package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "address")
class AddressEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = 0,

    @Column(name = "address")
    val address: String? = null,

    @Column(name = "address_mention", length = 256)
    val addressMention: @Size(max = 256) String? = null,

    @Column(name = "postal_code")
    val postalCode: String? = null,

    @Column(name = "city")
    val city: String? = null,

    @Column(name = "country")
        val country: String? = null,

    @Column(name = "last_date")
    var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: AddressDomain): AddressEntity {
    return AddressEntity(
            id = null,
            address = domain.address,
            addressMention = domain.addressMention!!,
            postalCode = domain.postalCode!!,
            city = domain.city!!,
            country = domain.country!!,
            lastDate = domain.lastDate!!
    )
}

fun fromEntity(entity: AddressEntity): AddressDomain {
    return AddressDomain(
            address = entity.address,
            addressMention = entity.addressMention,
            postalCode = entity.postalCode,
            city = entity.city,
            country = entity.country,
            lastDate = entity.lastDate
    )
}