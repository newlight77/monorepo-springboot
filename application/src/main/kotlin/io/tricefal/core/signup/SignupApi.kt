package io.tricefal.core.signup

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.math.sign

@RestController
@RequestMapping("signup")
class SignupApi(val signupWebHandler: SignupWebHandler) {

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

    @PostMapping("verify/email")
    @ResponseStatus(HttpStatus.OK)
    fun activateByToken(@RequestParam token: String): SignupStateModel {
        return signupWebHandler.activateByToken(token)
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