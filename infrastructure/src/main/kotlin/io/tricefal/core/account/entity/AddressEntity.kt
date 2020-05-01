package io.tricefal.core.account.entity

import io.tricefal.core.account.domain.AddressDomain
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "address")
class AddressEntity(
//                    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//                    @SequenceGenerator(name = "sequenceGenerator")
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    @Id
                    val id: Long = 0,

                    @Column(name = "postal_address")
                    val postalAddress: String = "",

                    @Column(name = "postal_address_mention", length = 256)
                    val postalAddressMention: @Size(max = 256) String = "",

                    @Column(name = "postal_postcode")
                    val postalPostcode: String = "",

                    @Column(name = "postal_city")
                    val postalCity: String = "",

                    @Column(name = "postal_country")
                    val postalCountry: String = ""
)

fun fromDomain(domain: AddressDomain): AddressEntity {
    return AddressEntity(
            domain.id,
            domain.postalAddress,
            domain.postalAddressMention,
            domain.postalPostcode,
            domain.postalCity,
            domain.postalCountry
    );
}

fun toDomain(entity: AddressEntity): AddressDomain {
    return AddressDomain(
            entity.id,
            entity.postalAddress,
            entity.postalAddressMention,
            entity.postalPostcode,
            entity.postalCity,
            entity.postalCountry
    );
}