package io.tricefal.core.security

//import org.keycloak.adapters.KeycloakConfigResolver
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
//import org.keycloak.adapters.springsecurity.KeycloakConfiguration
//import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
//import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
//import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.web.servlet.FilterRegistrationBean
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Profile
//import org.springframework.core.annotation.Order
//import org.springframework.http.HttpMethod
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.builders.WebSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
//import org.springframework.security.web.csrf.CsrfFilter
//import org.springframework.security.web.util.matcher.RequestMatcher
//import org.springframework.web.cors.CorsConfiguration


//@KeycloakConfiguration
//@Profile(value = ["prod", "dev", "local", "localhost"])
//@EnableGlobalMethodSecurity(
////        securedEnabled = true, // @Secured
//        jsr250Enabled = true // @RolesAllowed
////        prePostEnabled = true // @PreAuthorize and @PostAuthorize
//)
//@Order(1)
//class KeycloakConfiguration: KeycloakWebSecurityConfigurerAdapter() {
//
//    @Value("\${security.csrf.enabled}")
//    private val csrfEnabled = false
//
//    @Bean
//    fun keycloakConfigResolver(): KeycloakConfigResolver {
//        return KeycloakSpringBootConfigResolver()
//    }
//
//    @Bean
//    fun grantedAuthoritiesMapper(): GrantedAuthoritiesMapper? {
//        val mapper = SimpleAuthorityMapper()
//        mapper.setPrefix("ROLE_");
//        mapper.setConvertToLowerCase(true)
//        return mapper
//    }
//
//    @Autowired
//    @Throws(java.lang.Exception::class)
//    fun configureGlobal(auth: AuthenticationManagerBuilder) {
//        val provider = keycloakAuthenticationProvider()
//        auth.authenticationProvider(provider)
//        provider.setGrantedAuthoritiesMapper(grantedAuthoritiesMapper());
//    }
//
//    @Bean
//    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy? {
//        return NullAuthenticatedSessionStrategy()
////        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
//    }
//
//    @Bean
//    fun keycloakAuthenticationProcessingFilterRegistrationBean(
//            filter: KeycloakAuthenticationProcessingFilter?): FilterRegistrationBean<*>? {
//        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
//        registrationBean.isEnabled = false
//        return registrationBean
//    }
//
//    @Bean
//    fun keycloakPreAuthActionsFilterRegistrationBean(filter: KeycloakPreAuthActionsFilter?): FilterRegistrationBean<*>? {
//        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
//        registrationBean.isEnabled = false
//        return registrationBean
//    }
//
//    override fun configure(webSecurity: WebSecurity) {
//        webSecurity
//                .ignoring()
//                // All of Spring Security will ignore the requests
//                .antMatchers("/hello")
//                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html", "/v3/api-docs/**")
//                .antMatchers(HttpMethod.POST, "/logins")
//                .antMatchers(HttpMethod.POST, "/signup")
//                .antMatchers(HttpMethod.GET, "/signup/email/verify**")
//                .antMatchers(HttpMethod.GET, "/signup/state/*")
//                .antMatchers(HttpMethod.GET, "/pricer")
//    }
//
//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//        super.configure(http)
//
//        //@formatter:off
//        http
//                .cors() //
//                .configurationSource { //
//                    CorsConfiguration().applyPermitDefaultValues() //
//                } //
//                .and() //
//                .anonymous().disable() //
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
//                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()) //
//
//                // delegate logout endpoint to spring security
//                .and()
//                .logout().addLogoutHandler(keycloakLogoutHandler())
//                .logoutUrl("/logout").permitAll()
//
////                .and() //
////                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter::class.java) //
////                .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter::class.java) //
////                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()) //
//
//                .and().authorizeRequests() //
////                .antMatchers("/**").hasRole("user-role")
////                .antMatchers("/signup/upload/test").hasRole("user-role")
//                .antMatchers("/**").authenticated()
//                .anyRequest().denyAll() //
//
//
//        http.requiresChannel()
//                .requestMatchers(RequestMatcher { r ->
//                    r.getHeader("XSRF-TOKEN") != null
//                }).requiresSecure()
//
//        http.headers().frameOptions().disable()
//
//        if (!csrfEnabled)
//            http .csrf().disable()
//
////        http.addFilterAfter(CustomFilter(), CsrfFilter::class.java)
//        http.addFilterAfter(CustomFilter(), UsernamePasswordAuthenticationFilter::class.java)
//
////        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////        http.addFilterAfter(CsrfHeaderFilter(), UsernamePasswordAuthenticationFilter::class.java)
//
//        // need usename/password to auto-login during signup
////        http.addFilterAfter(JwtAuthorizationFilter(oktaJwtVerifier), UsernamePasswordAuthenticationFilter::class.java)
//        //@formatter:on
//    }
//
//
//}