package io.tricefal.core.user

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("user")
class UserApi(val userWebHandler: UserWebHandler,
                private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RolesAllowed("ROLE_user-role")
    @PutMapping("password")
    @ResponseStatus(HttpStatus.OK)
    fun updatePassword(principal: Principal, @RequestBody passwordModel : UserPasswordModel) {
        if (authenticatedUser(principal) === passwordModel.username)
            userWebHandler.updatePassword(authenticatedUser(principal), passwordModel.newPassword)
    }

    private fun authenticatedUser(principal: Principal): String {
        return principal.name
    }

}