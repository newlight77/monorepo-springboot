package io.tricefal.core.security

import com.okta.spring.boot.oauth.Okta
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableWebSecurity
@Order(1)
@Profile(value = ["prod", "dev"])
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var oktaJwtVerifier: OktaJwtVerifier

    override fun configure(webSecurity: WebSecurity) {
        webSecurity
            .ignoring()
                // All of Spring Security will ignore the requests
                .antMatchers("/hello")
                .antMatchers(HttpMethod.POST,"/logins")
                .antMatchers(HttpMethod.POST,"/signup")
                .antMatchers(HttpMethod.POST,"/signup/activate**")
                .antMatchers(HttpMethod.GET,"/signup/verify/email**")
                .antMatchers(HttpMethod.GET,"/signup/state**")
    }

    override fun configure(http: HttpSecurity) {

        //@formatter:off
        http
            .cors()
            .configurationSource {
                CorsConfiguration().applyPermitDefaultValues()
            }
            .and()
                .authorizeRequests()
                    .anyRequest().authenticated()
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

        Okta.configureResourceServer401ResponseBody(http);
        //@formatter:on
    }

}