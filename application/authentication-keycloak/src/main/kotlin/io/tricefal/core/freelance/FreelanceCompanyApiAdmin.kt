package io.tricefal.core.freelance

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("freelance")
class FreelanceCompanyApiAdmin(val freelanceWebHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun freelancers(): List<FreelanceModel> {
        return freelanceWebHandler.findAll()
    }

}