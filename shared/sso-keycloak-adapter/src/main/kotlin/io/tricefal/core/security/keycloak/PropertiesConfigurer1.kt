package io.tricefal.core.security.keycloak

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

import org.springframework.core.io.support.ResourcePatternUtils

import org.springframework.boot.env.YamlPropertySourceLoader

import org.springframework.core.env.ConfigurableEnvironment

import org.springframework.core.env.MutablePropertySources

import org.springframework.core.io.ResourceLoader

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.beans.factory.InitializingBean

import org.springframework.context.EnvironmentAware

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.Environment
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.function.Consumer

//
//@Component
//class PropertiesConfigurer1 : PropertySourcesPlaceholderConfigurer(), EnvironmentAware, InitializingBean {
//
////    private lateinit var locations: Array<String>
//
//    @Autowired
//    private lateinit var rl: ResourceLoader
//
//    private lateinit var environment: Environment
//
//    override fun setEnvironment(environment: Environment) {
//        this.environment = environment
//        super.setEnvironment(environment)
//    }
//
//    @Throws(Exception::class)
//    override fun afterPropertiesSet() {
//        // Copy property sources to Environment
//        val envPropSources = (environment as ConfigurableEnvironment).propertySources
//        envPropSources.map { propertySource ->
//            val propertyLocations = propertySource.getProperty("application.properties.locations") as String?
//            propertyLocations
//                ?.split(",")
//                ?.map { filename ->
//                    loadProperties(filename).forEach { source ->
//                        envPropSources.addFirst(source)
//                    }
//                }
//
//        }
//    }
//
////    @Throws(Exception::class)
////    override fun afterPropertiesSet() {
////        // Copy property sources to Environment
////        val envPropSources = (environment as ConfigurableEnvironment?)!!.propertySources
////        envPropSources.forEach(Consumer { propertySource: PropertySource<*> ->
////            if (propertySource.containsProperty("application.properties.locations")) {
////                locations =
////                    (propertySource.getProperty("application.properties.locations") as String?)!!.split(",")
////                        .toTypedArray()
////                Arrays.stream(locations).forEach { filename ->
////                    loadProperties(filename).forEach { source ->
////                        envPropSources.addFirst(source)
////                    }
////                }
////            }
////        })
////    }
//
//    private fun loadProperties(filename: String): List<PropertySource<*>> {
//        val loader = YamlPropertySourceLoader()
//        return try {
//            val possiblePropertiesResources: Array<Resource> =
//                ResourcePatternUtils.getResourcePatternResolver(rl).getResources(filename)
//            val properties = Properties()
//            Arrays.stream(possiblePropertiesResources)
//                .filter(Resource::exists)
//                .map { resource1 ->
//                    try {
//                        return@map loader.load(resource1.getFilename(), resource1)
//                    } catch (e: IOException) {
//                        throw RuntimeException(e)
//                    }
//                }.flatMap { l -> l.stream() }
//                .collect(Collectors.toList())
//        } catch (e: IOException) {
//            throw RuntimeException(e)
//        }
//    }
//
//}