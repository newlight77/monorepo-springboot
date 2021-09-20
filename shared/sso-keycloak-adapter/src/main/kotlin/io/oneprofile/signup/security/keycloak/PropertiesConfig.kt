package io.oneprofile.signup.security.keycloak

import org.springframework.core.io.PathResource

import org.springframework.core.io.ClassPathResource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import java.io.IOException
import java.util.Properties
import java.util.Arrays.stream
import java.util.stream.Collectors.toMap

//@Configuration
open class PropertiesConfig {

    private val logger: Logger = LoggerFactory.getLogger(PropertiesConfig::class.java)

    @Value("\${application.properties.locations}")
    private val propertiesLocation: String? = null

    @Bean
    open fun myProperties(): Map<String?, Properties?>? {
        return stream(arrayOf("spring-security-keycloak.yml"))
            .collect(
                toMap({ filename -> filename }) { filename: String -> loadPropertiesByFilename(filename) }
            )
    }

    private fun loadPropertiesByFilename(filename: String): Properties {
        val possiblePropertiesResources: Array<Resource> = arrayOf(
            ClassPathResource(filename),
            PathResource("config/$filename"),
            PathResource(filename),
            PathResource(getCustomPath(filename))
        )

        val resource: Resource = possiblePropertiesResources
            .filter(Resource::exists)
            .reduce { _, current -> current }

        return loadProperties(resource)
    }

    private fun loadProperties(resource: Resource): Properties {
        val properties = Properties()
        try {
            properties.load(resource.inputStream)
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
        logger.info("Using {} as user resource", resource)
        return properties
    }

    private fun getCustomPath(filename: String): String {
        logger.info("Using {} propertiesLocation", propertiesLocation)
        return if (propertiesLocation!!.endsWith(".yml")) propertiesLocation else propertiesLocation + filename
    }
}