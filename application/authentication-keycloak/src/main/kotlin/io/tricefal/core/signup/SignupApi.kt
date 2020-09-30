package io.tricefal.core.signup

import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("signup")
class SignupApi(val signupWebHandler: SignupWebHandler,
                private final val env: Environment) {

    private var frontendBaseUrl = env.getProperty("core.frontendUrl")!!

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody signup: SignupModel): SignupStateModel {
        return signupWebHandler.signup(signup)
    }

    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun signup(username: String): SignupModel {
        validateUser(username)
        return signupWebHandler.findByUsername(username).get()
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun signups(): List<SignupModel> {
        return signupWebHandler.findAll()
    }

    // admin
    @PostMapping("{username}/activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@PathVariable username: String): SignupStateModel {
        return signupWebHandler.activate(username)
    }

    // admin
    @PostMapping("{username}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    fun deactivate(@PathVariable username: String): SignupStateModel {
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
        validateUser(codeModel.username)
        return signupWebHandler.verifyByCode(codeModel.name, codeModel.code.toString())
    }

    @GetMapping("email/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByEmail(response: HttpServletResponse, @RequestParam token: String): ResponseEntity<Any> {
        val state = signupWebHandler.verifyByEmailFromToken(token)
        val url = frontendBaseUrl + "/register/activated/" + state.username
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, url).build()
    }

    @PostMapping("upload/portrait", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadPortrait(@RequestParam file : MultipartFile): SignupStateModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.uploadPortrait(authentication.name, file)
    }

    // upcoming frontend
    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(@RequestParam file : MultipartFile): SignupStateModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.uploadResume(authentication.name, file)
    }

    @PostMapping("upload/ref", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(@RequestParam file : MultipartFile): SignupStateModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.uploadRef(authentication.name, file)
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestBody statusModel : SignupStatusModel): SignupStateModel {
        validateUser(statusModel.username)
        val status = toStatus(statusModel.status)
        return signupWebHandler.updateStatus(statusModel.username, status)
    }

    private fun validateUser(username: String): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        if (!authentication.isAuthenticated() || authentication.name != username)
            throw IllegalArgumentException("username not expected")
        return username
    }
}