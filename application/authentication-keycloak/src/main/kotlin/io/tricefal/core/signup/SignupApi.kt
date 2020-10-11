package io.tricefal.core.signup

import org.keycloak.KeycloakPrincipal
import org.keycloak.representations.AccessToken
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("signup")
class SignupApi(val signupWebHandler: SignupWebHandler,
                private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private var frontendBaseUrl = env.getProperty("core.frontendUrl")!!

    // not-secure
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody signup: SignupModel): SignupStateModel {
        return signupWebHandler.signup(signup)
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSignup(principal: KeycloakPrincipal<*>) {
        signupWebHandler.delete(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun signup(username: String): SignupModel {
        return signupWebHandler.findByUsername(username).get()
    }

    @GetMapping("{username}/state")
    @ResponseStatus(HttpStatus.OK)
    fun state(principal: KeycloakPrincipal<*>, @PathVariable username : String): SignupStateModel {
        // make sure the username is the one authenticated
        return signupWebHandler.state(validateUser(principal, username))
    }

    @PostMapping("code/resend")
    @ResponseStatus(HttpStatus.OK)
    fun resendCode(principal: KeycloakPrincipal<*>, @RequestBody resendCodeModel : SignupCodeModel): SignupStateModel {
        logger.info("signup resend code requested")
        // make sure the username is the one authenticated
        return signupWebHandler.resendCode(authenticatedUser(principal))
    }

    @PostMapping("code/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByCode(principal: KeycloakPrincipal<*>, @RequestBody codeModel : SignupCodeModel): SignupStateModel {
        logger.info("signup activation by code requested")
        return signupWebHandler.verifyByCode(authenticatedUser(principal), codeModel.code.toString())
    }

    // not-secure
    @GetMapping("email/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByEmail(response: HttpServletResponse, @RequestParam token: String): ResponseEntity<Any> {
        logger.info("signup activation by email requested")
        val state = signupWebHandler.verifyByEmailFromToken(token)
        val url = frontendBaseUrl + "/register/activated/" + state.username
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, url).build()
    }

    // upcoming frontend
    @RolesAllowed("ROLE_user-role")
    @PostMapping("upload/portrait", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadPortrait(principal: KeycloakPrincipal<*>, @RequestParam file : MultipartFile): SignupStateModel {
        logger.info("signup uploading portrait requested")
        return signupWebHandler.uploadPortrait(authenticatedUser(principal), file)
    }

//    @PreAuthorize("hasRole('user-role')")
    @RolesAllowed("ROLE_user-role")
    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(principal: KeycloakPrincipal<*>, @RequestParam file : MultipartFile): SignupStateModel {
        logger.info("signup uploading cv requested")
        return signupWebHandler.uploadResume(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("upload/ref", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(principal: KeycloakPrincipal<*>, @RequestParam file : MultipartFile): SignupStateModel {
        logger.info("signup uploading ref requested")
        return signupWebHandler.uploadRef(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(principal: KeycloakPrincipal<*>, @RequestBody statusModel : SignupStatusModel): SignupStateModel {
        logger.info("signup updating status requested")
        val status = toStatus(statusModel.status)
        return signupWebHandler.updateStatus(authenticatedUser(principal), status)
    }

    private fun authenticatedUser(principal: KeycloakPrincipal<*>): String {
        val token: AccessToken = principal.keycloakSecurityContext.token
        return token.email
    }

    private fun validateUser(principal: KeycloakPrincipal<*>, username: String): String {
        val token: AccessToken = principal.keycloakSecurityContext.token

        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        if (!authentication.isAuthenticated || token.email != username)
            throw IllegalArgumentException("username not expected")
        return username
    }
}