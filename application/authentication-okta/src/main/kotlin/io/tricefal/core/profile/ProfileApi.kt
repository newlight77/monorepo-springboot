package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileModel
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("profile")
class ProfileApi(val profileWebHandler: ProfileWebHandler) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun profile(principal: Principal): ProfileModel {
        return profileWebHandler.find(authenticatedUser())
    }

    @PostMapping("portrait", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadPortrait(@RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadPortrait(authenticatedUser(), file)
    }

    @PostMapping("cv", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadResume(@RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadResume(authenticatedUser(), file)
    }

    @PostMapping("ref", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadResumeLinkedin(@RequestParam file: MultipartFile): ProfileModel {
        return profileWebHandler.uploadResumeLinkedin(authenticatedUser(), file)
    }

    @GetMapping("portrait")
    @ResponseStatus(HttpStatus.OK)
    fun downloadPortrait(response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.portrait(authenticatedUser())
        return toStreamingResponse(response, metafile)

    }

    @GetMapping("cv")
    @ResponseStatus(HttpStatus.OK)
    fun downloadResume(response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.resume(authenticatedUser())
        return toStreamingResponse(response, metafile)

    }

    @GetMapping("cvlinkedin")
    @ResponseStatus(HttpStatus.OK)
    fun downloadResumeLinkedin(response: HttpServletResponse): StreamingResponseBody {
        val metafile = profileWebHandler.resumeLinkedin(authenticatedUser())
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

    private fun authenticatedUser(): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return authentication.name
    }
}