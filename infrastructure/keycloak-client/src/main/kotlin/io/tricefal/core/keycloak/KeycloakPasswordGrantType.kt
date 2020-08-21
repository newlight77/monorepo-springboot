package io.tricefal.core.keycloak

import io.tricefal.core.signup.SignupDomain

class KeycloakPasswordGrantType (
        private val username: String,
        private val password: String,
        private val grantType: String,
        private val clientId: String,
        private val clientSecret: String
) {

    class Builder {
        private var username: String? = null
        private var password: String? = null
        private var grantType: String? = null
        private var clientId: String? = null
        private var clientSecret: String? = null

        fun username(username: String) =  apply { this.username = username }
        fun password(password: String) =  apply { this.password = password }
        fun grantType(grantType: String) =  apply { this.grantType = grantType }
        fun clientId(clientId: String) =  apply { this.clientId = clientId }
        fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret}

        fun build(): KeycloakPasswordGrantType {
            return KeycloakPasswordGrantType(username!!, password!!, "password", clientId!!, clientSecret!!)
        }

    }
}


fun toKeycloakPasswordGrantType(domain: SignupDomain): KeycloakPasswordGrantType {
    return KeycloakPasswordGrantType.Builder()
            .username(domain.username)
            .password(domain.password!!)
            .grantType("password")
            .clientId("")
            .clientSecret("")
            .build()
}
