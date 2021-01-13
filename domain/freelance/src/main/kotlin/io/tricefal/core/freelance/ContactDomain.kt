package io.tricefal.core.freelance

import java.time.Instant

class ContactDomain(
    val email: String?,
    val lastName: String?,
    val firstName: String?,
    val langKey: String? = null,
    val phone: String? = null,
    val landline: String? = null,
    val fax: String? = null,
    val email2: String? = null,
    var address: AddressDomain? = null,
    val lastDate: Instant? = null
) {
    data class Builder(
        var email: String? = null,
        var lastName: String? = null,
        var firstName: String? = null,
        var langKey: String? = null,
        var phone: String? = null,
        var landline: String? = null,
        var fax: String? = null,
        var email2: String? = null,
        var address: AddressDomain? = null,
        var lastDate: Instant? = null
    ) {
        fun email(email: String?) = apply { this.email = email }
        fun lastName(lastName: String?) = apply { this.lastName = lastName }
        fun firstName(firstName: String?) = apply { this.firstName = firstName }
        fun langKey(langKey: String?) = apply { this.langKey = langKey }
        fun phone(phone: String?) = apply { this.phone = phone }
        fun landline(landline: String?) = apply { this.landline = landline }
        fun fax(fax: String?) = apply { this.fax = fax }
        fun email2(email2: String?) = apply { this.email2 = email2 }
        fun address(address: AddressDomain?) = apply { this.address = address }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = ContactDomain(
                email = email,
                lastName = lastName,
                firstName = firstName,
                langKey = langKey,
                phone = phone,
                landline = landline,
                fax = fax,
                email2 = email2,
                address = address,
                lastDate = lastDate
        )
    }
}