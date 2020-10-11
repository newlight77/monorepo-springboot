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
class SignupApiAdmin(val signupWebHandler: SignupWebHandler,
                     private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private var frontendBaseUrl = env.getProperty("core.frontendUrl")!!

    // admin
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

}