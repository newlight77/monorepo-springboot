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
@RequestMapping("freelance/company")
class FreelanceCompanyApi(val freelanceWebHandler: FreelanceWebHandler) {

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

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/rib", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRib(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("signup uploading cv requested")
        return freelanceWebHandler.uploadRib(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/rc", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRc(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("signup uploading cv requested")
        return freelanceWebHandler.uploadRc(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/urssaf", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadUrssaf(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("signup uploading cv requested")
        return freelanceWebHandler.uploadUrssaf(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/fiscal", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadFiscal(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("signup uploading cv requested")
        return freelanceWebHandler.uploadFiscal(authenticatedUser(principal), file)
    }

    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
        return principal.name
    }

}