package io.tricefal.core.api

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("user")
class UserApi() {

    @GetMapping("/account")
    fun notes(principal: Principal): String {
        println("Fetching notes for user: ${principal.name}")
            return principal.name
    }

    @GetMapping("")
    fun user(@AuthenticationPrincipal user: OidcUser): OidcUser {
        return user;
    }

}