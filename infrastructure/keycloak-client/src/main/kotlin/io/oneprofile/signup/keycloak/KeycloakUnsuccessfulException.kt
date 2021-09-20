package io.oneprofile.signup.keycloak

class KeycloakUnsuccessfulException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}