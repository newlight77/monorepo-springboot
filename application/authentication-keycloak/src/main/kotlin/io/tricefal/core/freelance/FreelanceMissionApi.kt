package io.tricefal.core.freelance

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("freelance/mission")
class FreelanceMissionApi(val freelanceWebHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody signup: FreelanceModel): FreelanceModel {
        return freelanceWebHandler.create(signup)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun get(username: String): FreelanceModel {
        return freelanceWebHandler.findByUsername(username)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/kbis", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadKbis(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("signup uploading cv requested")
        return freelanceWebHandler.uploadKbis(authenticatedUser(principal), file)
    }


    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
        return principal.name
    }

}