package io.tricefal.core.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.impl.DefaultClaims
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val HEADER = "Authorization"
private const val PREFIX = "Bearer "
private const val SECRET = "mySecretKey"

class JwtAuthorizationFilter(val oktaJwtVerifier: OktaJwtVerifier) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            if (checkJWTToken(request)) {
                val claims: Claims = validateToken(request)
                if (claims["authorities"] != null) {
                    setUpSpringAuthentication(claims)
                } else {
                    SecurityContextHolder.clearContext()
                }
            }
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.localizedMessage)
            return
        }
    }

    private fun checkJWTToken(request: HttpServletRequest): Boolean {
        val authenticationHeader = request.getHeader(HEADER)
        return !(authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
    }

    private fun validateToken(request: HttpServletRequest): Claims {
        val jwtToken = request.getHeader(HEADER).replace(PREFIX, "")
        val jwt = oktaJwtVerifier.decode(jwtToken)
        logger.info("claims : ${jwt.claims.size}")
        jwt.claims.map { logger.info("claim : ${it.key}=${it.value}") }
        return DefaultClaims(jwt.claims)
//        return Jwts.parser().setSigningKey(SECRET.toByteArray()).parseClaimsJws(jwtToken).body
    }

    private fun setUpSpringAuthentication(claims: Claims) {
        logger.info("claims : $claims")
        val authorities = claims["authorities"] as List<*>
        val auth = UsernamePasswordAuthenticationToken(claims.subject, null,
                authorities.map { role -> SimpleGrantedAuthority(role.toString()) })
        SecurityContextHolder.getContext().authentication = auth
    }

}