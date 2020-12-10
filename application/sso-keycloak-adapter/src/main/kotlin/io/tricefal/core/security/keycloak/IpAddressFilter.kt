package io.tricefal.core.security.keycloak

import io.tricefal.core.security.ip.IpAddressEventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class IpAddressFilter(private val ipAddressEventHandler: IpAddressEventHandler) : OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse,
                                         chain: FilterChain) {
        if (filterIpAddress(req)) {
            // Skip the rest of the filters
            req.getRequestDispatcher(req.servletPath).forward(req, res)
        }
        chain.doFilter(req, res)
    }

    @Throws(Exception::class)
    fun filterIpAddress(request: HttpServletRequest) : Boolean {
        var ipAddress = request.getHeader("X-Forward-For")
        if (ipAddress == null) {
            ipAddress = request.remoteAddr
        }
        return ipAddressEventHandler.filter(ipAddress)
    }
}