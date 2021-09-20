package io.oneprofile.signup.security.keycloak

import io.oneprofile.signup.security.ip.*
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.core.type.AnnotationMetadata


class AuthenticationConfigurationRegistrar: ImportBeanDefinitionRegistrar {

    private fun annotation(): Class<out Annotation?>? {
        return SsoKeycloakAdapterConfiguration::class.java
    }

    override fun registerBeanDefinitions(
        importingMetadata: AnnotationMetadata,
        registry: BeanDefinitionRegistry
    ) {
        val attributes = attributesFor(importingMetadata, annotation())
        if (attributes != null) {

            val enableIpFilter = attributes["enableIpFilter"] as Boolean

            if (enableIpFilter) {
                val ipAddressRepositoryAdapter = BeanDefinitionBuilder
                    .rootBeanDefinition(IpAddressRepositoryAdapter::class.java)
                    .addDependsOn(IpAddressJpaRepository::class.java.simpleName).beanDefinition
                registry.registerBeanDefinition("ipAddressRepositoryAdapter", ipAddressRepositoryAdapter)

                val ipAddressJpaRepository = BeanDefinitionBuilder
                    .rootBeanDefinition(IpAddressJpaRepository::class.java).beanDefinition
                registry.registerBeanDefinition("ipAddressJpaRepository", ipAddressJpaRepository)

                val ipAddressEventHandler = BeanDefinitionBuilder
                    .rootBeanDefinition(IpAddressEventHandler::class.java).beanDefinition
                registry.registerBeanDefinition("ipAddressEventHandler", ipAddressEventHandler)

                val ipAddressEventPublisher = BeanDefinitionBuilder
                    .rootBeanDefinition(IpAddressEventPublisher::class.java).beanDefinition
                registry.registerBeanDefinition("ipAddressEventPublisher", ipAddressEventPublisher)

                val ipAddressEventListener = BeanDefinitionBuilder
                    .rootBeanDefinition(IpAddressEventListener::class.java).beanDefinition
                registry.registerBeanDefinition("ipAddressEventListener", ipAddressEventListener)
            }
        }
    }

    private fun attributesFor(metadata: AnnotatedTypeMetadata, annotationClass: Class<*>?): AnnotationAttributes? {
        return AnnotationAttributes.fromMap(
            metadata.getAnnotationAttributes(
                annotationClass!!.name, false
            )
        )
    }

}
