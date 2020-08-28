package io.tricefal.core

import io.tricefal.core.email.EmailService
import io.tricefal.core.keycloak.KeycloakAccountService
import io.tricefal.core.keycloak.KeycloakLoginService
import io.tricefal.core.metafile.MetafileRepository
import io.tricefal.core.twilio.SmsService
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles

@ComponentScan("io.tricefal.core")
@ActiveProfiles("test")
class InfrastructureMockBeans {
    
    @MockBean
    lateinit var keycloakAccountService: KeycloakAccountService

    @MockBean
    lateinit var keycloakLoginService: KeycloakLoginService

    @MockBean
    lateinit var emailService: EmailService

    @MockBean
    lateinit var smsService: SmsService

    @MockBean
    lateinit var metaFileRepository: MetafileRepository
}