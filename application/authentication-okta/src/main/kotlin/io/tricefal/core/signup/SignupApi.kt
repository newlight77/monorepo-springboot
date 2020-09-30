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

    // admin
    @PostMapping("{username}/activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@PathVariable username: String): SignupStateModel {
        return signupWebHandler.activate(username)
    }

    // admin
    @GetMapping("{username}/state")
    @ResponseStatus(HttpStatus.OK)
    fun state(@PathVariable username : String): SignupStateModel {
        return signupWebHandler.state(username)
    }

    @GetMapping("state")
    @ResponseStatus(HttpStatus.OK)
    fun state(): SignupStateModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return signupWebHandler.state(authentication.name)
    }

    @GetMapping("code/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByCode(@RequestBody activateModel: SignupActivateModel): SignupStateModel {
        return signupWebHandler.verifyByCode(activateModel.username, activateModel.code.toString())
    }

    @GetMapping("email/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyByEmail(response: HttpServletResponse, @RequestParam token: String): ResponseEntity<Any> {
        val state = signupWebHandler.verifyByToken(token)
        val url = frontendBaseUrl + "/register/activated/" + state.username
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, url).build()
    }

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
        return signupWebHandler.uploadResume(authentication.name, file)
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestBody statusModel : SignupStatusModel): SignupStateModel {
        val status = toStatus(statusModel.status)
        return signupWebHandler.updateStatus(statusModel.username, status)
    }

}