package io.tricefal.core.signup

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

    @RolesAllowed("ROLE_user-role")
    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun signup(username: String): SignupModel {
        validateUser(username)
        return signupWebHandler.findByUsername(username).get()
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun signups(): List<SignupModel> {
        return signupWebHandler.findAll()
    }

    // admin
    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{username}/activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@PathVariable username: String): SignupStateModel {
        logger.info("signup activation requested")
        return signupWebHandler.activate(username)
    }

    // admin
    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{username}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    fun deactivate(@PathVariable username: String): SignupStateModel {
        logger.info("signup deactivation requested")
        return signupWebHandler.deactivate(username)
    }

    @GetMapping("{username}/state")
    @ResponseStatus(HttpStatus.OK)
    fun state(@PathVariable username : String): SignupStateModel {
        validateUser(username)
        return signupWebHandler.state(username)
    }

    @PostMapping("code/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByCode(@RequestBody codeModel : SignupCodeModel): SignupStateModel {
        logger.info("signup activation by code requested")
        return signupWebHandler.verifyByCode(codeModel.username, codeModel.code.toString())
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
    fun uploadPortrait(@RequestParam file : MultipartFile): SignupStateModel {
        logger.info("signup uploading portrait requested")
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.uploadPortrait(authentication.name, file)
    }

//    @PreAuthorize("hasRole('user-role')")
    @RolesAllowed("ROLE_user-role")
    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(@RequestParam file : MultipartFile): SignupStateModel {
        logger.info("signup uploading cv requested")
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.uploadResume(authentication.name, file)
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("upload/ref", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(@RequestParam file : MultipartFile): SignupStateModel {
        logger.info("signup uploading ref requested")
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.uploadRef(authentication.name, file)
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestBody statusModel : SignupStatusModel): SignupStateModel {
        logger.info("signup updating status requested")
        validateUser(statusModel.username)
        val status = toStatus(statusModel.status)
        return signupWebHandler.updateStatus(statusModel.username, status)
    }

    private fun validateUser(username: String): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        if (!authentication.isAuthenticated || authentication.name != username)
            throw IllegalArgumentException("username not expected")
        return username
    }
}