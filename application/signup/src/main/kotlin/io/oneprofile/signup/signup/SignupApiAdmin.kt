package io.oneprofile.signup.signup

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("signup")
class SignupApiAdmin(val signupWebHandler: SignupWebHandler,
                     private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private var frontendBaseUrl = env.getProperty("signup.frontendUrl")!!

    @RolesAllowed("ROLE_ac_oneprofile_r")
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

    @RolesAllowed("ROLE_ac_oneprofile_w")
    @PutMapping("{username}/comment")
    @ResponseStatus(HttpStatus.OK)
    fun addComment(principal: Principal, @PathVariable targetUsername: String, @RequestBody comment: CommentModel): CommentModel {
        logger.info("signup activation requested")
        comment.author = authenticatedUser(principal)
        return signupWebHandler.addComment(targetUsername, comment)
    }

    @RolesAllowed("ROLE_ac_oneprofile_w")
    @PutMapping("{username}/activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@PathVariable username: String): SignupStateModel {
        logger.info("signup activation requested")
        return signupWebHandler.activate(username)
    }

    @RolesAllowed("ROLE_ac_oneprofile_w")
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

    private fun authenticatedUser(principal: Principal): String {
        return principal.name
    }
}
