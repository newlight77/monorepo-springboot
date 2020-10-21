package io.tricefal.core.profile

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.core.env.Environment
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("profile")
class ProfileApi(val profileWebHandler: ProfileWebHandler,
                 private final val env: Environment) {

    private var frontendBaseUrl = env.getProperty("core.frontendUrl")!!

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
    fun uploadRef(principal: Principal, @RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadRef(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("portrait")
    @ResponseStatus(HttpStatus.OK)
    fun downloadPortrait(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.portrait(authenticatedUser(principal))
        response.contentType = metafile.contentType
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        val inputStream =  FileInputStream(Paths.get(metafile.filename).toFile())
        return streamingResponseBody(inputStream)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("cv")
    @ResponseStatus(HttpStatus.OK)
    fun downloadCv(principal: Principal): ResponseEntity<ByteArrayResource> {
        val metafile = profileWebHandler.cv(authenticatedUser(principal))

        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        header.add("Cache-Control", "no-cache, no-store, must-revalidate")
        header.add("Pragma", "no-cache")
        header.add("Expires", "0")

        val path = Paths.get(metafile.filename)
        val resource = ByteArrayResource(Files.readAllBytes(path))

        return ResponseEntity.ok()
                .contentLength(metafile.size)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("ref")
    @ResponseStatus(HttpStatus.OK)
    fun downloadRef(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.ref(authenticatedUser(principal))
        response.contentType = metafile.contentType
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        val inputStream =  FileInputStream(Paths.get(metafile.filename).toFile())
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