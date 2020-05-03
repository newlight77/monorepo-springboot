package io.tricefal.core.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@EnableWebSecurity
@Profile(value = ["production"])
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var oktaJwtVerifier: OktaJwtVerifier

    override fun configure(http: HttpSecurity) {

        //@formatter:off
        http
            .cors()
            .configurationSource {
                CorsConfiguration().applyPermitDefaultValues()
            }
            .and()
            .authorizeRequests().anyRequest().authenticated()
                .and()
            .oauth2Login()
                .and()
            .oauth2ResourceServer().jwt()

        http.requiresChannel()
            .requestMatchers(RequestMatcher {
                r -> r.getHeader("XSRF-TOKEN") != null
            }).requiresSecure()

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        http.addFilterAfter(CsrfHeaderFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAfter(JwtAuthorizationFilter(oktaJwtVerifier), UsernamePasswordAuthenticationFilter::class.java)

        //@formatter:on
    }

    private fun csrfTokenRepository(): CsrfTokenRepository? {
        val repository = HttpSessionCsrfTokenRepository()
        repository.setHeaderName("X-XSRF-TOKEN")
        return repository
    }

}