package io.tricefal.core.account.model

import io.tricefal.core.account.domain.ContactDomain

data class ContactModel(
        var id: Long,
        val lastName: String,
        val firstName: String,
        var langKey: String,
        var imageUrl: String,
        val address: AddressModel,
        val phone: String,
        val fax: String,
        val landline: String,
        val email: String,
        val email2: String
)

fun fromDomain(domain: ContactDomain): ContactModel {
    return ContactModel(
            domain.id,
            domain.lastName,
            domain.firstName,
            domain.langKey,
            domain.imageUrl,
            fromDomain(domain.address),
            domain.phone,
            domain.fax,
            domain.landline,
            domain.email,
            domain.email2
    )
}

fun toDomain(model: ContactModel): ContactDomain {
    return ContactDomain(
            model.id,
            model.lastName,
            model.firstName,
            model.langKey,
            model.imageUrl,
            toDomain(model.address),
            model.phone,
            model.fax,
            model.landline,
            model.email,
            model.email2
    )
}