package io.tricefal.core.freelance

data class AddressModel(var address: String,
                        var addressMention: String?,
                        var postalCode: String?,
                        var city: String?,
                        var country: String?
) {
    data class Builder(
            val address: String,
            var addressMention: String? = null,
            var postalCode: String? = null,
            var city: String? = null,
            var country: String? = null
    ) {
        fun addressMention(addressMention: String?) = apply { this.addressMention = addressMention }
        fun postalCode(postalCode: String?) = apply { this.postalCode = postalCode }
        fun city(city: String?) = apply { this.city = city }
        fun country(country: String?) = apply { this.country = country }

        fun build() = AddressModel(
                address = address,
                addressMention = addressMention,
                postalCode = postalCode,
                city = city,
                country = country
        )
    }
}

fun toModel(domain: AddressDomain): AddressModel {
    return AddressModel(
            address = domain.address,
            addressMention = domain.addressMention,
            postalCode = domain.postalCode,
            city = domain.city,
            country = domain.country
    )
}

fun fromModel(model: AddressModel): AddressDomain {
    return AddressDomain(
            address = model.address,
            addressMention = model.addressMention,
            postalCode = model.postalCode,
            city = model.city,
            country = model.country
    )
}