package io.tricefal.core.freelance

data class ContactModel(
        val email: String,
        var lastName: String?,
        var firstName: String?,
        var langKey: String? = null,
        var phone: String? = null,
        var landline: String? = null,
        var fax: String? = null,
        var email2: String? = null,
        var address: AddressModel? = null
) {
    fun lastName(lastName: String?) = apply { this.lastName = lastName }
    fun firstName(firstName: String?) = apply { this.firstName = firstName }
    fun langKey(langKey: String?) = apply { this.langKey = langKey }
    fun phone(phone: String?) = apply { this.phone = phone }
    fun landline(landline: String?) = apply { this.landline = landline }
    fun fax(fax: String?) = apply { this.fax = fax }
    fun email2(email2: String?) = apply { this.email2 = email2 }
    fun address(address: AddressModel?) = apply { this.address = address }

    fun build() = ContactModel(
            email,
            lastName,
            firstName,
            langKey,
            phone,
            landline,
            fax,
            email2,
            address
    )
}

fun toModel(domain: ContactDomain): ContactModel {
    return ContactModel(
            domain.email,
            domain.lastName,
            domain.firstName,
            domain.langKey,
            domain.phone,
            domain.landline,
            domain.fax,
            domain.email2,
            domain.address?.let { toModel(it) }
    )
}

fun fromModel(model: ContactModel): ContactDomain {
    return ContactDomain(
            model.email,
            model.lastName,
            model.firstName,
            model.langKey,
            model.phone,
            model.landline,
            model.fax,
            model.email2,
            model.address?.let { fromModel(it) }
    )
}