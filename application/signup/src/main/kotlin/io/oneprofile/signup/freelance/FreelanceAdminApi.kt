package io.oneprofile.signup.freelance

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("freelance")
class FreelanceAdminApi(val freelanceWebHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun freelancers(): List<FreelanceModel> {
        return freelanceWebHandler.findAll()
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable username: String): FreelanceModel {
        return freelanceWebHandler.findByUsername(username)
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("availables")
    @ResponseStatus(HttpStatus.OK)
    fun availables(): List<FreelanceModel> {
        return freelanceWebHandler.availables()
    }

}