package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("mission")
class MissionAdminApi(val missionWishWebHandler: MissionWishWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    fun findByUsername(@PathVariable username: String): MissionWishModel {
        return missionWishWebHandler.findByUsername(username)
    }

    @RolesAllowed("ROLE_ac_freelance_r")
    @GetMapping("list")
    @ResponseStatus(HttpStatus.OK)
    fun all(): List<MissionWishModel> {
        return missionWishWebHandler.findAll()
    }

}