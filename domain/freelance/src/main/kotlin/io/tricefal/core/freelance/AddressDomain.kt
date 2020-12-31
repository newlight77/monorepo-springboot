package io.tricefal.core.freelance

import java.time.Instant

data class AddressDomain(val address: String,
                         var addressMention: String?,
                         val postalCode: String?,
                         val city: String?,
                         val country: String?,
                         val lastDate: Instant?)
{
    data class Builder(
            val address: String,
            var addressMention: String? = null,
            var postalCode: String? = null,
            var city: String? = null,
            var country: String? = null,
            var lastDate: Instant? = null
    ) {
        fun addressMention(addressMention: String?) = apply { this.addressMention = addressMention }
        fun postalCode(postalCode: String?) = apply { this.postalCode = postalCode }
        fun city(city: String?) = apply { this.city = city }
        fun country(country: String?) = apply { this.country = country }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = AddressDomain(
                address = address,
                addressMention = addressMention,
                postalCode = postalCode,
                city = city,
                country = country,
                lastDate = lastDate
        )
    }
}

