package io.tricefal.core.freelance

data class AddressDomain(val address: String,
                         var addressMention: String?,
                         val postalCode: String,
                         val city: String,
                         val country: String)
{
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

        fun build() = AddressDomain(
                address,
                addressMention,
                postalCode!!,
                city!!,
                country!!
        )
    }
}

