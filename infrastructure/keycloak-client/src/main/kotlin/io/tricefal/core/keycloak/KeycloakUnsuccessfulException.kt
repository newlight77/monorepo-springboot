package io.tricefal.core.keycloak

class KeycloakUnsuccessfulException(private val msg: String) : Throwable(msg) {
}