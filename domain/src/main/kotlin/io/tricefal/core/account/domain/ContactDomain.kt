package io.tricefal.core.account.domain

class ContactDomain(
        var id: Long,
        val lastName: String,
        val firstName: String,
        var langKey: String,
        var imageUrl: String,
        val address: AddressDomain,
        val phone: String,
        val fax: String,
        val landline: String,
        val email: String,
        val email2: String
) {
}