package io.tricefal.core

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import java.util.*

@Configuration
class WebMvcConfigurer: WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        val lci = LocaleChangeInterceptor()
        lci.paramName = "i18n"
        registry.addInterceptor(lci)
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        val slr = CookieLocaleResolver()
        slr.setDefaultLocale(Locale.FRANCE)
        return slr
    }

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:i18n/messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}