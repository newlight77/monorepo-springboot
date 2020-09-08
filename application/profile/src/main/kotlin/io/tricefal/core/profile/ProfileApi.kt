package io.tricefal.core.profile

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
@RequestMapping("profile")
class ProfileApi(val profileWebHandler: ProfileWebHandler,
                 private final val env: Environment) {

    private var frontendBaseUrl = env.getProperty("core.frontendUrl")!!

    @PostMapping("portrait", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadPortrai(@RequestParam file : MultipartFile): ProfileModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return profileWebHandler.uploadPortrait(authentication.name, file)
    }

    @PostMapping("cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(@RequestParam file : MultipartFile): ProfileModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return profileWebHandler.uploadResume(authentication.name, file)
    }

    @PostMapping("ref", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(@RequestParam file : MultipartFile): ProfileModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return profileWebHandler.uploadRef(authentication.name, file)
    }

}