package io.tricefal.core.keycloak

data class KeycloakTokenResponse (
        val accessToken: String,
        val expiresIn: Long,
        val notBeforePolicy: Int,
        val refreshExpiresIn: Long,
        val refreshToken: Long,
        val scope: String,
        val sessionState: String,
        val tokenType: String
)

data class KeycloakRegisterResponse (
        val code: String
)