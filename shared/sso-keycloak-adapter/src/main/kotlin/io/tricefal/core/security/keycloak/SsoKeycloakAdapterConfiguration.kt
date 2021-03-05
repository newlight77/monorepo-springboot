package io.tricefal.core.security.keycloak

import io.tricefal.core.security.SsoKeycloakComponent
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
