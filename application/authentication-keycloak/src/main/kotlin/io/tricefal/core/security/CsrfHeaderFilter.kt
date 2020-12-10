package io.tricefal.core.security

//import org.springframework.security.web.csrf.CsrfToken
//import org.springframework.web.filter.OncePerRequestFilter
//import org.springframework.web.util.WebUtils
//import javax.servlet.FilterChain
//import javax.servlet.http.Cookie
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse

//class CsrfHeaderFilter : OncePerRequestFilter() {
//
//    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
////        addHeader(response)
//        val csrf: CsrfToken = request.getAttribute(CsrfToken::class.java.name) as CsrfToken
//        var cookie: Cookie? = WebUtils.getCookie(request, "XSRF-TOKEN")
//        val token: String = csrf.token
//        if (cookie == null || token != cookie.value) {
//            cookie = Cookie("XSRF-TOKEN", token)
//            cookie.path = "/"
//            response.addCookie(cookie)
//        }
//        filterChain.doFilter(request, response)
//    }
//
////    fun addHeader(response: HttpServletResponse) {
////        response.addHeader("Content-Security-Policy", "frame-ancestors 'self' http://localhost:1080")
////        response.addHeader("X-Frame-Options", "SAMEORIGIN")
//////        response.addHeader("X-Frame-Options", "ALLOW-FROM http://localhost:1080")
////    }
//
//}