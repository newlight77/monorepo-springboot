package io.oneprofile.signup.cgu

//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("cgu")
class CguApi(val cguWebHandler: CguWebHandler,
             private final val env: Environment) {

    @RolesAllowed("ROLE_user-role")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun find(principal: Principal): CguModel {
        return cguWebHandler.find(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("{cguVersion}/accept")
    @ResponseStatus(HttpStatus.OK)
    fun acceptCgu(principal: Principal, @PathVariable cguVersion: String): CguModel {
        return cguWebHandler.acceptCgu(authenticatedUser(principal), cguVersion)
    }

    private fun authenticatedUser(principal: Principal): String {
        return principal.name
    }

}