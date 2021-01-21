package io.tricefal.core.company

import io.tricefal.core.freelance.CompanyModel
import io.tricefal.core.freelance.FreelanceModel
import io.tricefal.core.metafile.MetafileModel
import io.tricefal.shared.util.json.PatchOperation
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
@RequestMapping("/company")
class CompanyApi(val companyWebHandler: CompanyWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody company: CompanyModel): CompanyModel {
        return companyWebHandler.create(company)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{companyName}/completed")
    @ResponseStatus(HttpStatus.OK)
    fun completed(@PathVariable companyName: String): CompanyModel {
        return companyWebHandler.completed(companyName)
    }

    @RolesAllowed("ROLE_ac_tricefal_r")
    @GetMapping("{companyName}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable companyName: String): CompanyModel {
        return companyWebHandler.findByName(companyName)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PutMapping("{companyName}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable companyName: String, @RequestBody company: CompanyModel): CompanyModel {
        return companyWebHandler.update(companyName, company)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PatchMapping("{companyName}")
    @ResponseStatus(HttpStatus.OK)
    fun patch(@PathVariable companyName: String, @RequestBody patchOperations: List<PatchOperation>): CompanyModel {
        return companyWebHandler.patch(companyName, patchOperations)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{companyName}/upload/kbis", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadKbis(@PathVariable companyName: String, @RequestParam file : MultipartFile): CompanyModel {
        logger.info("company uploading kbis requested")
        return companyWebHandler.uploadKbis(companyName, file)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{companyName}/upload/rib", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRib(@PathVariable companyName: String, @RequestParam file : MultipartFile): CompanyModel {
        logger.info("company uploading rib requested")
        return companyWebHandler.uploadRib(companyName, file)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{companyName}/upload/rc", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRc(@PathVariable companyName: String, @RequestParam file : MultipartFile): CompanyModel {
        logger.info("company uploading rc requested")
        return companyWebHandler.uploadRc(companyName, file)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{companyName}/upload/urssaf", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadUrssaf(@PathVariable companyName: String, @RequestParam file : MultipartFile): CompanyModel {
        logger.info("company uploading urssaf requested")
        return companyWebHandler.uploadUrssaf(companyName, file)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @PostMapping("{companyName}/upload/fiscal", consumes = [ "multipart/form-data" ])
    @ResponseStatus(HttpStatus.OK)
    fun uploadFiscal(@PathVariable companyName: String, @RequestParam file : MultipartFile): CompanyModel {
        logger.info("company uploading fiscal requested")
        return companyWebHandler.uploadFiscal(companyName, file)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("{companyName}/kbis")
    @ResponseStatus(HttpStatus.OK)
    fun downloadKbis(@PathVariable companyName: String, response: HttpServletResponse): StreamingResponseBody {
        val metafile = companyWebHandler.kbis(companyName)
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("{companyName}/rib")
    @ResponseStatus(HttpStatus.OK)
    fun downloadRib(@PathVariable companyName: String, response: HttpServletResponse): StreamingResponseBody {
        val metafile = companyWebHandler.rib(companyName)
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("{companyName}/rc")
    @ResponseStatus(HttpStatus.OK)
    fun downloadRc(@PathVariable companyName: String, response: HttpServletResponse): StreamingResponseBody {
        val metafile = companyWebHandler.rc(companyName)
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("{companyName}/urssaf")
    @ResponseStatus(HttpStatus.OK)
    fun downloadUrssaf(@PathVariable companyName: String, response: HttpServletResponse): StreamingResponseBody {
        val metafile = companyWebHandler.urssaf(companyName)
        return toStreamingResponse(response, metafile)
    }

    @RolesAllowed("ROLE_ac_tricefal_w")
    @GetMapping("{companyName}/fiscal")
    @ResponseStatus(HttpStatus.OK)
    fun downloadfiscal(@PathVariable companyName: String, response: HttpServletResponse): StreamingResponseBody {
        val metafile = companyWebHandler.fiscal(companyName)
        return toStreamingResponse(response, metafile)
    }

    private fun toStreamingResponse(response: HttpServletResponse, metafile: MetafileModel): StreamingResponseBody {
        response.contentType = metafile.contentType
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        response.setHeader("filename", metafile.filename)
        response.setHeader("updateDate", metafile.creationDate.toString())
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