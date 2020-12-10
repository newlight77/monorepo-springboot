package io.tricefal.core.mission

import io.tricefal.core.metafile.MetafileModel
//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.slf4j.LoggerFactory
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
@RequestMapping("mission")
class MissionWishApi(val missionWishWebHandler: MissionWishWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody missionWish: MissionWishModel): MissionWishModel {
        return missionWishWebHandler.create(missionWish)
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun get(principal: Principal): MissionWishModel {
        return missionWishWebHandler.findByUsername(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun update(principal: Principal, @RequestBody missionWish: MissionWishModel): MissionWishModel {
        return missionWishWebHandler.update(missionWish)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("cv")
    @ResponseStatus(HttpStatus.OK)
    fun downloadResume(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = missionWishWebHandler.resume(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/cv", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(principal: Principal, @RequestParam file : MultipartFile): MissionWishModel {
        logger.info("signup uploading cv requested")
        return missionWishWebHandler.updateResume(authenticatedUser(principal), file)
    }

    private fun authenticatedUser(principal: Principal): String {
//        if (principal is KeycloakAuthenticationToken) {
//            return principal.account.keycloakSecurityContext.token.email
//        }
        return principal.name
    }

    private fun toStreamingResponse(response: HttpServletResponse, metafile: MetafileModel): StreamingResponseBody {
        response.contentType = metafile.contentType
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        response.setHeader("filename", metafile.filename)
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

}