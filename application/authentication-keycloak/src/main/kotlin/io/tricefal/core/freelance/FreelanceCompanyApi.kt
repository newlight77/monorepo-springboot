package io.tricefal.core.freelance

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("freelance/company")
class FreelanceCompanyApi(val freelanceWebHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody freelance: FreelanceModel): FreelanceModel {
        return freelanceWebHandler.create(freelance)
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun get(principal: Principal): FreelanceModel {
        return freelanceWebHandler.findByUsername(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/kbis", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadKbis(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading kbis requested")
        return freelanceWebHandler.uploadKbis(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/rib", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRib(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading rib requested")
        return freelanceWebHandler.uploadRib(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/rc", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRc(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading rc requested")
        return freelanceWebHandler.uploadRc(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/urssaf", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadUrssaf(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading urssaf requested")
        return freelanceWebHandler.uploadUrssaf(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/fiscal", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadFiscal(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading fiscal requested")
        return freelanceWebHandler.uploadFiscal(authenticatedUser(principal), file)
    }

    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
        return principal.name
    }

}