package io.tricefal.core.freelance

import java.time.Instant

data class ContactModel(
        val email: String?,
        var lastName: String?,
        var firstName: String?,
        var langKey: String? = null,
        var phone: String? = null,
        var landline: String? = null,
        var fax: String? = null,
        var email2: String? = null,
        var lastDate: Instant? = null
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
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = ContactModel(
            email = email,
            lastName = lastName,
            firstName = firstName,
            langKey = langKey,
            phone = phone,
            landline = landline,
            fax = fax,
            email2 = email2,
            lastDate = lastDate
        )
    }
}

fun toModel(domain: ContactDomain): ContactModel {
    return ContactModel(
            email = domain.email,
            lastName = domain.lastName,
            firstName = domain.firstName,
            langKey = domain.langKey,
            phone = domain.phone,
            landline = domain.landline,
            fax = domain.fax,
            email2 = domain.email2,
            lastDate = domain.lastDate
    )
}

fun fromModel(model: ContactModel): ContactDomain {
    return ContactDomain(
            email = model.email,
            lastName = model.lastName,
            firstName = model.firstName,
            langKey = model.langKey,
            phone = model.phone,
            landline = model.landline,
            fax = model.fax,
            email2 = model.email2,
            lastDate = model.lastDate
    )
}