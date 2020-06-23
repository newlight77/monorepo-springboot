package io.tricefal.core.signup

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("signup")
class SignupApi(val signupHandler: SignupHandler) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody signup: SignupModel): SignupStateModel {
        return signupHandler.signup(signup)
    }

    @PostMapping("activate")
    @ResponseStatus(HttpStatus.OK)
    fun activate(@RequestParam username : String, @RequestParam code: Int): SignupStateModel {
        return signupHandler.activate(username, code.toString())
    }

    @PostMapping("upload")
    @ResponseStatus(HttpStatus.OK)
    fun upload(@RequestParam username : String, @RequestBody file: MultipartFile): SignupStateModel {
        return signupHandler.uploadResume(username, file)
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    fun updateStatus(@RequestParam username : String, @RequestParam status: String): SignupStateModel {
        return signupHandler.updateStatus(username, status)
    }

}