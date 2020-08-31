package io.tricefal.core.account.model

import io.tricefal.core.account.domain.AddressDomain

data class AddressModel(var id: Long,
                     var postalAddress: String,
                     var postalAddressMention: String,
                     var postalPostcode: String,
                     var postalCity: String,
                     var postalCountry: String)

fun fromDomain(domain: AddressDomain): AddressModel {
    return AddressModel(
            domain.id,
            domain.postalAddress,
            domain.postalAddressMention,
            domain.postalPostcode,
            domain.postalCity,
            domain.postalCountry
    )
}


fun toDomain(model: AddressModel): AddressDomain {
    return AddressDomain(
            model.id,
            model.postalAddress,
            model.postalAddressMention,
            model.postalPostcode,
            model.postalCity,
            model.postalCountry
    )
}