package io.tricefal.core.profile

//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("profile")
class ProfileAdminApi(val profileWebHandler: ProfileWebHandler) {

    @RolesAllowed("ROLE_ac_tricefal_r")
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
    }

}