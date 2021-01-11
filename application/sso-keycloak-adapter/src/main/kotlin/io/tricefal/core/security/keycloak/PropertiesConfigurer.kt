package io.tricefal.core.security.keycloak

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.EnvironmentAware
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class PropertiesConfigurer : PropertySourcesPlaceholderConfigurer(), EnvironmentAware, InitializingBean {

    private val logger: Logger = LoggerFactory.getLogger(PropertiesConfig::class.java)

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    private lateinit var environment: Environment

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
        super.setEnvironment(environment)
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        // Copy property sources to Environment
        val envPropSources: MutablePropertySources = (environment as ConfigurableEnvironment).propertySources

        envPropSources.map {
            with(logger) {
                info("property name : ${it.name}")
                info("property source class : ${it.source.javaClass}")
            }
        }

        envPropSources.map { propertySource ->
            val propertyLocations = propertySource.getProperty("application.properties.locations") as String?
            logger.info("propertyLocations are $propertyLocations")
            propertyLocations
                ?.split(",")
                ?.map { filename -> loadProperties(filename) }
                ?.flatten()
                ?.forEach { source ->
                    envPropSources.addFirst(source)
                }
        }

    }

    private fun loadProperties(filename: String): List<PropertySource<*>> {
        return try {
            ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(filename)
                .filter(Resource::exists)
                .map { loadResource(it) }
                .flatten()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun loadResource(resource: Resource, ): List<PropertySource<*>> {
        try {
            return YamlPropertySourceLoader().load(resource.filename, resource)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

}