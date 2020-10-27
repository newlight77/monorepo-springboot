package io.tricefal.core.freelance

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed


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