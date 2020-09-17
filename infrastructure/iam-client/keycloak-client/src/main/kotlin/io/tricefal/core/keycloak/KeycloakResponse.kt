package io.tricefal.core.keycloak

import com.google.gson.annotations.SerializedName

data class KeycloakTokenResponse (
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("expires_in") val expiresIn: Long,
        @SerializedName("refresh_expires_in") val refreshExpiresIn: Long,
        @SerializedName("refresh_token") val refreshToken: String,
        @SerializedName("token_type") val tokenType: String,
        @SerializedName("not-before-policy") val notBeforePolicy: Int,
        @SerializedName("session_state") val sessionState: String,
        val scope: String
)

data class KeycloakRegisterResponse (
        val code: String
)