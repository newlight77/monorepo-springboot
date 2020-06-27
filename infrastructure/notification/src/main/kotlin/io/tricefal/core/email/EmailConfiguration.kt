package io.tricefal.core.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean


@Configuration
@PropertySource("classpath:email.yml")
class EmailConfiguration {

    @Autowired
    private lateinit var env: Environment

//    @Bean
//    fun freeMarkerConfiguration(): FreeMarkerConfigurationFactoryBean? {
//        val bean = FreeMarkerConfigurationFactoryBean()
//        bean.setTemplateLoaderPath("classpath:/templates/")
//        bean.setDefaultEncoding("UTF-8")
//        return bean
//    }

    @Bean
    fun javaMailSender(): JavaMailSender {
        val sender = JavaMailSenderImpl()
        sender.port = env.getProperty("spring.mail.port")!!.toInt()
        sender.host = env.getProperty("spring.mail.host")!!
        return sender
    }
}