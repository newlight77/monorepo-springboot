package io.oneprofile.core.security.keycloak

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