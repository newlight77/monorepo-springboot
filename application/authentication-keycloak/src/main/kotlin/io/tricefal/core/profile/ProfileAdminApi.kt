package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileModel
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
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
class ProfileAdminApi(val profileWebHandler: ProfileWebHandler) {

    @RolesAllowed("ROLE_user-role")
    @GetMapping("{username}/cv")
    @ResponseStatus(HttpStatus.OK)
    fun downloadCv(@PathVariable username: String): ResponseEntity<ByteArrayResource> {
        val metafile = profileWebHandler.resume(username)

        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        header.add("Cache-Control", "no-cache, no-store, must-revalidate")
        header.add("Pragma", "no-cache")
        header.add("Expires", "0")

        val path = Paths.get(metafile.filename)
        val resource = ByteArrayResource(Files.readAllBytes(path))

        return ResponseEntity.ok()
                .contentLength(metafile.size!!)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
    }core/application/authentication-keycloak/src/main/kotlin/io/tricefal/core/profile/ProfileApi.kt

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