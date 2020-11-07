package io.tricefal.core.mission

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("mission")
class MissionWishApi(val missionWishWebHandler: MissionWishWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody missionWish: MissionWishModel): MissionWishModel {
        return missionWishWebHandler.create(missionWish)
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun get(principal: Principal): MissionWishModel {
        return missionWishWebHandler.findByUsername(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun update(principal: Principal, @RequestBody missionWish: MissionWishModel): MissionWishModel {
        return missionWishWebHandler.update(missionWish)
    }

    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
        return principal.name
    }

}