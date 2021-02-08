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

    @RolesAllowed("ROLE_ac_tricefal_r")
    @GetMapping("list")
    @ResponseStatus(HttpStatus.OK)
    fun all(@RequestParam status: String?): List<SignupModel> {
        if (status.isNullOrBlank()) return signupWebHandler.findAll().filter { it.status != Status.NONE }
        return when (status.toUpperCase()) {
            "REGISTERED" -> signupWebHandler.findAll().filter { it.status != Status.NONE }
            "NONE" -> signupWebHandler.findAll().filter { it.status == Status.NONE }
            "CLIENT" -> signupWebHandler.findAll().filter { it.status == Status.CLIENT }
            "EMPLOYEE" -> signupWebHandler.findAll().filter { it.status == Status.EMPLOYEE }
            "FREELANCE" -> signupWebHandler.findAll().filter { it.status == Status.FREELANCE  || it.status == Status.FREELANCE_WITH_MISSION }
            "FREELANCE_WITH_MISSION" -> signupWebHandler.findAll().filter { it.status == Status.FREELANCE_WITH_MISSION }
            else -> signupWebHandler.findAll()
        }
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PutMapping("{username}/comment")
    @ResponseStatus(HttpStatus.OK)
    fun addComment(@PathVariable username: String, @RequestBody comment: CommentModel): CommentModel {
        logger.info("signup activation requested")
        return signupWebHandler.addComment(username, comment)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PutMapping("{username}/activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@PathVariable username: String): SignupStateModel {
        logger.info("signup activation requested")
        return signupWebHandler.activate(username)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PutMapping("{username}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    fun deactivate(@PathVariable username: String): SignupStateModel {
        logger.info("signup deactivation requested")
        return signupWebHandler.deactivate(username)
    }

    @DeleteMapping("{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSignup(@PathVariable username: String, @RequestParam authorizationCode: String?) {
        signupWebHandler.delete(username, authorizationCode)
    }

}