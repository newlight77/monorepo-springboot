package io.tricefal.core.signup

import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @PostMapping("activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@RequestParam username : String, @RequestParam code: Int): SignupStateModel {
        return signupWebHandler.activate(username, code.toString())
    }

    @GetMapping("state")
    @ResponseStatus(HttpStatus.OK)
    fun state(@RequestParam username : String): SignupStateModel {
        return signupWebHandler.state(username)
    }

    @GetMapping("verify/email")
    @ResponseStatus(HttpStatus.OK)
    fun activateByToken(response: HttpServletResponse, @RequestParam token: String): ResponseEntity<Any> {
        val state = signupWebHandler.activateByToken(token)
        val url = frontendBaseUrl + "/register/activated/" + state.username
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, url).build()
    }

    @PostMapping("upload")
    @ResponseStatus(HttpStatus.OK)
    fun upload(@RequestParam username : String, @RequestBody file: MultipartFile): SignupStateModel {
        return signupWebHandler.uploadResume(username, file)
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestParam username : String, @RequestParam status: String): SignupStateModel {
        return signupWebHandler.updateStatus(username, status)
    }

}