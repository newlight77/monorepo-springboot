package io.oneprofile.core.security.keycloak

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.annotation.Bean
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import java.io.IOException
import java.util.Properties
import java.util.Arrays.stream
import java.util.stream.Collectors.toMap



//@Configuration
open class PropertiesConfiguration {

    private val logger: Logger = LoggerFactory.getLogger(PropertiesConfig::class.java)

    @Value("\${application.properties.locations}")
    private lateinit var locations: Array<String>

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Bean
    open fun myProperties(): Map<String?, Properties?>? {
        return stream(locations)
            .collect(toMap({ filename -> filename }) { filename: String -> loadProperties(filename) })
    }

    private fun loadProperties(filename: String): Properties? {
        return try {
            val properties = Properties()
            ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(filename)
                .filter(Resource::exists)
                .map { loadResource(it) }
                .flatten()
                .forEach { properties.putAll( (it as MapPropertySource).source ) }
            properties

//            stream(possiblePropertiesResources)
//                .filter(Resource::exists)
//                .map { loadResource(it) }
////                .map { resource -> return@map loadResource(loader, resource) }
//                .flatMap { list -> list.stream() }
//                .forEach { propertySource ->
////                    val source: Map<*, *> = (propertySource as MapPropertySource).source
//                    properties.putAll((propertySource as MapPropertySource).source)
//                }
//            properties
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