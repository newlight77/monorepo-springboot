package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileModel
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.FileInputStream
import java.nio.file.Paths
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("profile")
class ProfileApi(val profileWebHandler: ProfileWebHandler) {

    @RolesAllowed("ROLE_user-role")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun profile(principal: Principal): ProfileModel {
        return profileWebHandler.find(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("portrait", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadPortrai(principal: Principal, @RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadPortrait(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("cv", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(principal: Principal, @RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadResume(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_user-role")
    @PostMapping("ref", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadResumeLinkedin(principal: Principal, @RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadResumeLinkedin(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("portrait")
    @ResponseStatus(HttpStatus.OK)
    fun downloadPortrait(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.portrait(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("cv")
    @ResponseStatus(HttpStatus.OK)
    fun downloadResume(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.resume(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("cvlinkedin")
    @ResponseStatus(HttpStatus.OK)
    fun downloadResumeLinkedin(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.resumeLinkedIn(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    private fun toStreamingResponse(response: HttpServletResponse, metafile: MetafileModel): StreamingResponseBody {
        response.contentType = metafile.contentType
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        val inputStream = FileInputStream(Paths.get(metafile.filename).toFile())
        return streamingResponseBody(inputStream)
    }

    private fun streamingResponseBody(inputStream: FileInputStream): StreamingResponseBody {
        return StreamingResponseBody { outputStream ->
            var bytesRead: Int
            val buffer = ByteArray(2048)
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }

    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
        return principal.name
    }

}