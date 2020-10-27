package io.tricefal.core.signup

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
    fun deleteSignup() {
        signupWebHandler.delete(authenticatedUser())
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun signup(): SignupModel {
        return signupWebHandler.findByUsername(authenticatedUser())
    }

    @GetMapping("state")
    @ResponseStatus(HttpStatus.OK)
    fun state(): SignupStateModel {
        return signupWebHandler.state(authenticatedUser())
    }

    @ResponseStatus(HttpStatus.OK)
    fun resendCode(@RequestBody resendCodeModel : SignupCodeModel): SignupStateModel {
        logger.info("signup resend code requested")
        return signupWebHandler.resendCode(authenticatedUser())
    }

    @GetMapping("code/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByCode(@RequestBody activateModel: SignupActivateModel): SignupStateModel {
        logger.info("signup activation by code requested")
        return signupWebHandler.verifyByCode(activateModel.username, activateModel.code.toString())
    }

    // not-secure
    @GetMapping("email/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByEmail(response: HttpServletResponse, @RequestParam token: String): ResponseEntity<Any> {
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
        return signupWebHandler.uploadPortrait(authenticatedUser(), file)
    }

    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(@RequestParam file : MultipartFile): SignupStateModel {
        return signupWebHandler.uploadResume(authenticatedUser(), file)
    }

    @PostMapping("upload/cvlinkedin", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(@RequestParam file : MultipartFile): SignupStateModel {
        return signupWebHandler.uploadResumeLinkedin(authenticatedUser(), file)
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestBody statusModel : SignupStatusModel): SignupStateModel {
        val status = toStatus(statusModel.status)
        return signupWebHandler.updateStatus(statusModel.username, status)
    }

    private fun authenticatedUser(): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return authentication.name
    }
}