package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileModel
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
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
@RequestMapping("freelance/company")
class FreelanceCompanyApi(val freelanceWebHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody freelance: FreelanceModel): FreelanceModel {
        return freelanceWebHandler.create(freelance)
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun get(principal: Principal): FreelanceModel {
        return freelanceWebHandler.findByUsername(authenticatedUser(principal))
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/kbis", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadKbis(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading kbis requested")
        return freelanceWebHandler.uploadKbis(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/rib", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRib(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading rib requested")
        return freelanceWebHandler.uploadRib(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/rc", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRc(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading rc requested")
        return freelanceWebHandler.uploadRc(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/urssaf", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadUrssaf(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading urssaf requested")
        return freelanceWebHandler.uploadUrssaf(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @PostMapping("upload/fiscal", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadFiscal(principal: Principal, @RequestParam file : MultipartFile): FreelanceModel {
        logger.info("freelance uploading fiscal requested")
        return freelanceWebHandler.uploadFiscal(authenticatedUser(principal), file)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("kbis")
    @ResponseStatus(HttpStatus.OK)
    fun downloadKbis(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = freelanceWebHandler.kbis(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("rib")
    @ResponseStatus(HttpStatus.OK)
    fun downloadRib(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = freelanceWebHandler.rib(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("rc")
    @ResponseStatus(HttpStatus.OK)
    fun downloadRc(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = freelanceWebHandler.rc(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("urssaf")
    @ResponseStatus(HttpStatus.OK)
    fun downloadUrssaf(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = freelanceWebHandler.urssaf(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_freelance_w")
    @GetMapping("fiscal")
    @ResponseStatus(HttpStatus.OK)
    fun downloadfiscal(principal: Principal, response: HttpServletResponse): StreamingResponseBody {
        val metafile = freelanceWebHandler.fiscal(authenticatedUser(principal))
        return toStreamingResponse(response, metafile)
    }

    private fun authenticatedUser(principal: Principal): String {
        if (principal is KeycloakAuthenticationToken) {
            return principal.account.keycloakSecurityContext.token.email
        }
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