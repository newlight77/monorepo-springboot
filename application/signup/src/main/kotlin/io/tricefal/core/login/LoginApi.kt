package io.tricefal.core.login

//import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServletServerHttpRequest
//import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.Instant
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("logins")
class LoginApi(val loginHandler: LoginWebHandler) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun login(request: HttpServletRequest, @RequestBody login: LoginModel) {
        print("saving login : $login")
        if (login.loginDate == null) login.loginDate = Instant.now()
        if (login.ipAddress == null) login.ipAddress = request.remoteAddr
        if (login.device == null) login.device = request.getHeader("user-agent")
        loginHandler.login(login)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun list(principal: Principal) : List<LoginModel>{
        return loginHandler.findByUsername(authenticatedUser(principal))
    }

    private fun authenticatedUser(principal: Principal): String {
        return principal.name
    }
}