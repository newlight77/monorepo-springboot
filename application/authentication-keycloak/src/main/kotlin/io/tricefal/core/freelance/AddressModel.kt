package io.tricefal.core.freelance

import java.time.Instant

data class AddressModel(var address: String?,
                        var addressMention: String?,
                        var postalCode: String?,
                        var city: String?,
                        var country: String?,
                        var lastDate: Instant?
) {
    data class Builder(
            var address: String? = null,
            var addressMention: String? = null,
            var postalCode: String? = null,
            var city: String? = null,
            var country: String? = null,
            var lastDate: Instant? = null
    ) {
        fun address(address: String?) = apply { this.address = address }
        fun addressMention(addressMention: String?) = apply { this.addressMention = addressMention }
        fun postalCode(postalCode: String?) = apply { this.postalCode = postalCode }
        fun city(city: String?) = apply { this.city = city }
        fun country(country: String?) = apply { this.country = country }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = AddressModel(
                address = address,
                addressMention = addressMention,
                postalCode = postalCode,
                city = city,
                country = country,
                lastDate = lastDate
        )
    }
}

fun toModel(domain: AddressDomain): AddressModel {
    return AddressModel(
            address = domain.address,
            addressMention = domain.addressMention,
            postalCode = domain.postalCode,
            city = domain.city,
            country = domain.country,
            lastDate = domain.lastDate
    )
}

fun fromModel(model: AddressModel): AddressDomain {
    return AddressDomain(
            address = model.address,
            addressMention = model.addressMention,
            postalCode = model.postalCode,
            city = model.city,
            country = model.country,
            lastDate = model.lastDate
    )
}