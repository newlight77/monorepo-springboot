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
                address,
                addressMention,
                postalCode,
                city,
                country
        )
    }
}

fun toModel(domain: AddressDomain): AddressModel {
    return AddressModel(
            domain.address,
            domain.addressMention,
            domain.postalCode,
            domain.city,
            domain.country
    )
}

fun fromModel(model: AddressModel): AddressDomain {
    return AddressDomain(
            model.address,
            model.addressMention,
            model.postalCode,
            model.city,
            model.country
    )
}