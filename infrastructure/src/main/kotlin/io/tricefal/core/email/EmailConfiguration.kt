package io.tricefal.core.email

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean


@Configuration
class EmailConfiguration {
    @Bean
    fun freeMarkerConfiguration(): FreeMarkerConfigurationFactoryBean? {
        val bean = FreeMarkerConfigurationFactoryBean()
        bean.setTemplateLoaderPath("classpath:/templates/")
        return bean
    }
}