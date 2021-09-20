package io.oneprofile.signup.security

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.annotation.security.RolesAllowed

@CrossOrigin("*")
@RestController
@RequestMapping("keycload")
open class KeycloakUserApi() {

    @RolesAllowed("ROLE_user-role")
    @GetMapping("user")
    fun principal(principal: Principal): String {
        println("Fetching user name: ${principal.name}")

        if (principal is KeycloakAuthenticationToken) {
            val token = principal.account.keycloakSecurityContext.token
            return token.name
        }

        return principal.name
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("/certs")
    fun certs(principal: Principal): String {
        println("Fetching notes for user: ${principal.name}")

        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.trustedCertificates.toString()
        }

        return "no certs"
    }
}