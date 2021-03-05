package io.tricefal.core.security.keycloak

import io.tricefal.core.security.ip.IpAddressEventHandler
import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration


@KeycloakConfiguration
@Profile(value = ["prod", "dev", "local", "localhost"])
@PropertySource("classpath:/spring-security-keycloak.yml")
@EnableGlobalMethodSecurity(
//        securedEnabled = true, // @Secured
        jsr250Enabled = true // @RolesAllowed
//        prePostEnabled = true // @PreAuthorize and @PostAuthorize
)
@Order(1)
open class SsoKeycloakConfigurerAdapter: KeycloakWebSecurityConfigurerAdapter() {

    @Value("\${security.csrf.enabled}")
    private val csrfEnabled = false

    @Autowired
    private lateinit var ipAddressEventHandler: IpAddressEventHandler

    @Bean
    open fun keycloakConfigResolver(): KeycloakConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

    @Bean
    open fun grantedAuthoritiesMapper(): GrantedAuthoritiesMapper? {
        val mapper = SimpleAuthorityMapper()
        mapper.setPrefix("ROLE_");
        mapper.setConvertToLowerCase(true)
        return mapper
    }

    @Autowired
    @Throws(java.lang.Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val provider = keycloakAuthenticationProvider()
        auth.authenticationProvider(provider)
        provider.setGrantedAuthoritiesMapper(grantedAuthoritiesMapper());
    }

    @Bean
    open override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy? {
        return NullAuthenticatedSessionStrategy()
//        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    open fun keycloakAuthenticationProcessingFilterRegistrationBean(
            filter: KeycloakAuthenticationProcessingFilter?): FilterRegistrationBean<*>? {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    open fun keycloakPreAuthActionsFilterRegistrationBean(filter: KeycloakPreAuthActionsFilter?): FilterRegistrationBean<*>? {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    override fun configure(webSecurity: WebSecurity) {
        webSecurity
            .ignoring()
            // All of Spring Security will ignore the requests
            .antMatchers("/hello")

            .antMatchers(HttpMethod.GET, "/actuator/**") //

            .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html", "/v3/api-docs/**") //

            .antMatchers(HttpMethod.POST, "/logins") //
            .antMatchers(HttpMethod.POST, "/notification/contact") //
            .antMatchers(HttpMethod.POST, "/notification/feedback") //
            .antMatchers(HttpMethod.POST, "/signup") //
            .antMatchers(HttpMethod.GET, "/signup/email/verify**") //
            .antMatchers(HttpMethod.GET, "/signup/state/*") //
            .antMatchers(HttpMethod.GET, "/pricer") //
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)

        //@formatter:off
        http
                .cors() //
                .configurationSource { //
                    CorsConfiguration().applyPermitDefaultValues() //
                } //
                .and() //
                .anonymous().disable() //
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()) //

                // delegate logout endpoint to spring security
                .and() //
                .logout().addLogoutHandler(keycloakLogoutHandler()) //
                .logoutUrl("/logout").permitAll() //


                .and().authorizeRequests() //
                .antMatchers("/**").authenticated() //
                .anyRequest().denyAll() //


        http.requiresChannel() //
                .requestMatchers(RequestMatcher { r -> //
                    r.getHeader("XSRF-TOKEN") != null //
                }).requiresSecure() //

        http.headers().frameOptions().disable()

        if (!csrfEnabled)
            http .csrf().disable()

        http.addFilterAfter(CustomFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAfter(IpAddressFilter(ipAddressEventHandler), UsernamePasswordAuthenticationFilter::class.java)

        //@formatter:on
    }


}