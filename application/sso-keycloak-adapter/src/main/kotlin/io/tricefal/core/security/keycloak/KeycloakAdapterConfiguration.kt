package io.tricefal.core.security.keycloak

import io.tricefal.core.security.CustomKeycloakComponent
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ComponentScan(basePackageClasses = [CustomKeycloakComponent::class])
//@Import(AuthenticationConfigurationRegistrar::class)
@Configuration
annotation class KeycloakAdapterConfiguration(
    val enableIpFilter: Boolean = true
)
