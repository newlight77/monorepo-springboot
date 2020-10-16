package io.tricefal.core.freelance

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.representations.AccessToken
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("freelance")
class FreelanceApi(val freelanceWebHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_tricefal_w-role")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody signup: FreelanceModel): FreelanceModel {
        return freelanceWebHandler.signup(signup)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun signup(username: String): FreelanceModel {
        return freelanceWebHandler.findByUsername(username).get()
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("signup uploading cv requested")
        return freelanceWebHandler.uploadResume(authenticatedUser(principal), file)
    }

    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
        return principal.name
    }

}