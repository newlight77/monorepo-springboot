package io.tricefal.core.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.impl.DefaultClaims
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(private val oktaJwtVerifier: OktaJwtVerifier) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            doFilter(jwtTokenInHeader(request))
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.localizedMessage)
            return
        }
    }

    private fun doFilter(jwtToken: String) {
        if (jwtToken.isBlank()) return

        val claims = extractClaims(jwtToken)

        if (claims["authorities"] != null) {
            setUpSpringAuthentication(claims)
        } else {
            SecurityContextHolder.clearContext()
        }
    }

    private fun jwtTokenInHeader(request: HttpServletRequest): String {
        val authorizationHeader = request.getHeader("Authorization")
        return if (StringUtils.isEmpty(authorizationHeader)) ""
        else authorizationHeader.replace("Bearer ", "")
    }

    private fun extractClaims(jwtToken: String): Claims {
        val jwt = oktaJwtVerifier.getOrDecode(jwtToken)
        logger.info("claims : ${jwt!!.claims.size}")
        jwt.claims.map { logger.info("claim : ${it.key}=${it.value}") }
        return DefaultClaims(jwt.claims)
    }

    private fun setUpSpringAuthentication(claims: Claims) {
        logger.info("claims : $claims")
        val authorities = claims["authorities"] as List<*>
        val auth = UsernamePasswordAuthenticationToken(claims.subject, null,
                authorities.map { role -> SimpleGrantedAuthority(role.toString()) })
        SecurityContextHolder.getContext().authentication = auth
    }

}