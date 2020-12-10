package io.tricefal.core.security

//import org.springframework.boot.web.servlet.FilterRegistrationBean
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.Ordered
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import org.springframework.web.filter.CorsFilter

//@Configuration
//class CorsConfiguration {
//
//    @Bean
//    fun autoConfigCorsFilter(): FilterRegistrationBean<CorsFilter> {
//        val config = CorsConfiguration().applyPermitDefaultValues()
//        config.allowCredentials = true
//        config.allowedOrigins = listOf("*", "http://localhost:4200")
//        config.allowedMethods = listOf("*")
//        config.allowedHeaders = listOf("*")
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", config)
//        val bean = FilterRegistrationBean(CorsFilter(source))
//        bean.order = Ordered.HIGHEST_PRECEDENCE
//        return bean
//    }
//}
