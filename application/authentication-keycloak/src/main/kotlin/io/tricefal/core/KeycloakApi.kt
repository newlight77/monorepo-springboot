package io.tricefal.core

import org.keycloak.KeycloakPrincipal
import org.keycloak.representations.AccessToken
import org.springframework.web.bind.annotation.*

@CrossOrigin("*")
@RestController
@RequestMapping("keycloak")
class KeycloakApi {
    @RequestMapping("", method = [RequestMethod.GET])
    @ResponseBody
    fun getUserInformation(principal: KeycloakPrincipal<*>): String {
        val token: AccessToken = principal.keycloakSecurityContext.token
        return "hello ${token.givenName} ${token.familyName}"
    }
}