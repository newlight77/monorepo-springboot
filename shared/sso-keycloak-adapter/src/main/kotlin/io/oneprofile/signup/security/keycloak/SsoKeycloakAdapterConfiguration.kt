package io.oneprofile.signup.security.keycloak

import io.oneprofile.signup.security.SsoKeycloakComponent
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ComponentScan(basePackageClasses = [SsoKeycloakComponent::class])
//@Import(AuthenticationConfigurationRegistrar::class)
@Configuration
annotation class SsoKeycloakAdapterConfiguration(
    val enableIpFilter: Boolean = true
)
