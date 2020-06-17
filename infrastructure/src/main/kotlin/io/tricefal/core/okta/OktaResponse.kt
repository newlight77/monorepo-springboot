package io.tricefal.core.okta

data class OktaResponse (
        val _links: Links,
        val activated: Any,
        val created: String,
        val credentials: Credentials,
        val id: String,
        val lastLogin: Any,
        val lastUpdated: String,
        val passwordChanged: String,
        val profile: Profile,
        val status: String,
        val statusChanged: Any
)

data class Links (
        val activate: Activate,
        val self: Self
)

data class Credentials (
        val password: Password,
        val provider: Provider
)

data class Profile (
        val email: String,
        val firstName: String,
        val lastName: String,
        val login: String,
        val mobilePhone: String
)

data class Activate (
        val href: String
)

data class Self (
        val href: String
)

class Password

data class Provider (
        val name: String,
        val type: String
)