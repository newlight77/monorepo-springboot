package io.oneprofile.signup


import io.oneprofile.signup.email.EmailService
import io.oneprofile.signup.keycloak.KeycloakRegistrationService
import io.oneprofile.signup.twilio.SmsService
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles

@ComponentScan("io.oneprofile.signup")
@ActiveProfiles("test")
class InfrastructureMockBeans {
    @MockBean
    lateinit var keycloakRegistrationService: KeycloakRegistrationService

    @MockBean
    lateinit var emailService: EmailService

    @MockBean
    lateinit var smsService: SmsService

//    @Bean
//    fun messageSource(): MessageSource {
//        val messageSource = ReloadableResourceBundleMessageSource()
//        messageSource.setBasename("classpath:i18n/messages")
//        messageSource.setDefaultEncoding("UTF-8")
//        return messageSource
//    }
}