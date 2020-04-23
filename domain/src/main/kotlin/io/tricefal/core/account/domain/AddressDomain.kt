package io.tricefal.core.account.domain

data class AddressDomain(var id: Long,
                         var postalAddress: String,
                         var postalAddressMention: String,
                         var postalPostcode: String,
                         var postalCity: String,
                         var postalCountry: String)


