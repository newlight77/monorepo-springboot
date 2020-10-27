package io.tricefal.core.profile

import org.springframework.core.env.Environment
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
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("profile")
class ProfileApi(val profileWebHandler: ProfileWebHandler) {

    @PostMapping("portrait", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadPortrai(@RequestParam file: MultipartFile): ProfileModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return profileWebHandler.uploadPortrait(authentication.name, file)
    }

    @PostMapping("cv", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadCv(@RequestParam file: MultipartFile): ProfileModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return profileWebHandler.uploadResume(authentication.name, file)
    }

    @PostMapping("ref", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.OK)
    fun uploadRef(@RequestParam file: MultipartFile): ProfileModel {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return profileWebHandler.uploadRef(authentication.name, file)
    }

    @GetMapping("portrait")
    @ResponseStatus(HttpStatus.OK)
    fun downloadPortrait(response: HttpServletResponse): StreamingResponseBody {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        profileWebHandler.portrait(authentication.name)

        val metafile = profileWebHandler.portrait(authentication.name)
        response.contentType = metafile.contentType
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${metafile.filename}")
        val inputStream =  FileInputStream(Paths.get(metafile.filename).toFile())
        return streamingResponseBody(inputStream)
    }

    @GetMapping("cv")
    @ResponseStatus(HttpStatus.OK)
    fun downloadCv(): ResponseEntity<ByteArrayResource> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val metafile = profileWebHandler.cv(authentication.name)

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
                .body(resource);
    }

    @GetMapping("ref")
    @ResponseStatus(HttpStatus.OK)
    fun downloadRef(response: HttpServletResponse): StreamingResponseBody {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val metafile = profileWebHandler.ref(authentication.name)
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

}