package io.tricefal.core.security

import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.RefreshableKeycloakSecurityContext
import org.keycloak.adapters.spi.KeycloakAccount
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse


@Configuration
class CustomFilter : OncePerRequestFilter() {

    @Value("\${security.enabled}")
    val securityEnabled: Boolean = true

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse,
                                         chain: FilterChain) {

        val wrapper: HttpServletRequestWrapper = wrapRequest(req)
//        if (req.getHeader("Authorization") != null && req.getHeader("Authorization").length < 20) {
//            wrapper.getRequestDispatcher(wrapper.servletPath).forward(wrapper, res)
//        }

        if (!securityEnabled) {
            val roles: Set<String> = hashSetOf()
            val session = RefreshableKeycloakSecurityContext(null, null, null, null, null, null, null)
            val principal: KeycloakPrincipal<RefreshableKeycloakSecurityContext> = KeycloakPrincipal("dummy", session)
            val account: KeycloakAccount = SimpleKeycloakAccount(principal, roles, principal.keycloakSecurityContext)
            val context: SecurityContext = SecurityContextHolder.createEmptyContext()
            context.authentication = KeycloakAuthenticationToken(account, false)
            SecurityContextHolder.setContext(context)

            // Skip the rest of the filters
            wrapper.getRequestDispatcher(wrapper.servletPath).forward(wrapper, res)
        }
        chain.doFilter(wrapper, res)
    }

    private fun wrapRequest(req: HttpServletRequest): HttpServletRequestWrapper {
        return object : HttpServletRequestWrapper(req) {
            override fun getHeader(name: String): String? {
                return if ("Authorization" == name && super.getHeader(name) != null && super.getHeader(name).length < 20) null
                else super.getHeader(name)
            }
        }
    }
}