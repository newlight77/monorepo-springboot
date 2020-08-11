package io.tricefal.core.account.api

import org.keycloak.KeycloakPrincipal
import org.keycloak.representations.AccessToken
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("keycloak")
class KeycloakApi {
    @RequestMapping("", method = [RequestMethod.GET])
    @ResponseBody
    fun getUserInformation(principal: KeycloakPrincipal<*>): String {
        val token: AccessToken = principal.keycloakSecurityContext.token
        val id: String = token.getId()
        val firstName: String = token.getGivenName()
        val lastName: String = token.getFamilyName()
        return token.toString()
    }
}