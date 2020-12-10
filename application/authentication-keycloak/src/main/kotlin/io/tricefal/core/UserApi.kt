package io.tricefal.core
//
//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
//import org.springframework.security.core.annotation.AuthenticationPrincipal
//import org.springframework.security.oauth2.core.oidc.user.OidcUser
//import org.springframework.web.bind.annotation.*
//import java.security.Principal
//import javax.annotation.security.RolesAllowed
//
//@CrossOrigin("*")
//@RestController
//@RequestMapping("user")
//class UserApi() {
//
//    @RolesAllowed("ROLE_user-role")
//    @GetMapping("")
//    fun principal(principal: Principal): String {
//        println("Fetching user name: ${principal.name}")
//
//        if (principal is KeycloakAuthenticationToken) {
//            val token = principal.account.keycloakSecurityContext.token
//            return token.name
//        }
//
//        if (principal is OidcUser) {
//            return  principal.idToken.fullName
//        }
//
//        return principal.name
//    }
//
//    @RolesAllowed("ROLE_user-role")
//    @GetMapping("/certs")
//    fun certs(principal: Principal): String {
//        println("Fetching notes for user: ${principal.name}")
//
//        if (principal is KeycloakAuthenticationToken) {
//            return principal.account.keycloakSecurityContext.token.trustedCertificates.toString()
//        }
//
//        return "no certs"
//    }
//}