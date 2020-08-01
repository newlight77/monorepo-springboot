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
    fun activate(@RequestBody activateModel: SignupActivateModel): SignupStateModel {
        return signupWebHandler.activate(activateModel.username, activateModel.code.toString())
    }

    @GetMapping("state/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun state(@PathVariable username : String): SignupStateModel {
        return signupWebHandler.state(username)
    }

    @GetMapping("verify/email")
    @ResponseStatus(HttpStatus.OK)
    fun everifyByEmail(response: HttpServletResponse, @RequestParam token: String): ResponseEntity<Any> {
        val state = signupWebHandler.verifyByToken(token)
        val url = frontendBaseUrl + "/register/activated/" + state.username
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, url).build()
    }

    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(@RequestParam username: String, @RequestParam file : MultipartFile): SignupStateModel {
        return signupWebHandler.uploadResume(username, file)
    }

    @PostMapping("upload/ref", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(@RequestParam username: String, @RequestParam file : MultipartFile): SignupStateModel {
        return signupWebHandler.uploadResume(username, file)
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestBody statusModel : SignupStatusModel): SignupStateModel {
        return signupWebHandler.updateStatus(statusModel.username, statusModel.status)
    }

}