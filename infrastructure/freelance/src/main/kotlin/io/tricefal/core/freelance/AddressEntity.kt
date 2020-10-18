package io.tricefal.core.freelance

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "address")
class AddressEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = 0,

    @Column(name = "address")
    val address: String = "",

    @Column(name = "address_mention", length = 256)
    val addressMention: @Size(max = 256) String = "",

    @Column(name = "postal_code")
    val postalCode: String = "",

    @Column(name = "city")
    val city: String = "",

    @Column(name = "country")
        val country: String = ""
)

fun toEntity(domain: AddressDomain): AddressEntity {
    return AddressEntity(
            null,
            domain.address,
            domain.addressMention!!,
            domain.postalCode,
            domain.city,
            domain.country
    );
}

fun fromEntity(entity: AddressEntity): AddressDomain {
    return AddressDomain(
            entity.address,
            entity.addressMention,
            entity.postalCode,
            entity.city,
            entity.country
    );
}