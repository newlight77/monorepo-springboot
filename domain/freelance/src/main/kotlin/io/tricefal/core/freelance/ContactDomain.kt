package io.tricefal.core.freelance

class ContactDomain(
        val email: String,
        val lastName: String?,
        val firstName: String?,
        var langKey: String? = null,
        val phone: String? = null,
        val landline: String? = null,
        val fax: String? = null,
        val email2: String? = null,
        val address: AddressDomain? = null
) {
    data class Builder(
            val email: String,
            var lastName: String? = null,
            var firstName: String? = null,
            var langKey: String? = null,
            var phone: String? = null,
            var landline: String? = null,
            var fax: String? = null,
            var email2: String? = null,
            var address: AddressDomain? = null
    ) {
        fun lastName(lastName: String?) = apply { this.lastName = lastName }
        fun firstName(firstName: String?) = apply { this.firstName = firstName }
        fun langKey(langKey: String?) = apply { this.langKey = langKey }
        fun phone(phone: String?) = apply { this.phone = phone }
        fun landline(landline: String?) = apply { this.landline = landline }
        fun fax(fax: String?) = apply { this.fax = fax }
        fun email2(email2: String?) = apply { this.email2 = email2 }
        fun address(address: AddressDomain?) = apply { this.address = address }

        fun build() = ContactDomain(
                email = email,
                lastName = lastName,
                firstName = firstName,
                langKey = langKey,
                phone = phone,
                landline = landline,
                fax = fax,
                email2 = email2,
                address = address
        )
    }
}