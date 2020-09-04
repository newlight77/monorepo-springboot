package io.tricefal.core


import io.tricefal.core.email.EmailService
import io.tricefal.core.okta.OktaService
import io.tricefal.core.twilio.SmsService
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles

@ComponentScan("io.tricefal.core")
@ActiveProfiles("test")
class InfrastructureMockBeans {
    @MockBean
    lateinit var oktaService: OktaService

    @MockBean
    lateinit var emailService: EmailService

    @MockBean
    lateinit var smsService: SmsService

}