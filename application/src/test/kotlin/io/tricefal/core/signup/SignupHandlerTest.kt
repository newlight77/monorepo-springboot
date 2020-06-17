package io.tricefal.core.signup

import io.tricefal.core.email.EmailService
import io.tricefal.core.metafile.MetafileRepository
import io.tricefal.core.okta.OktaService
import io.tricefal.core.twilio.SmsService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
class SignupHandlerTest {

    @Mock
    lateinit var service: ISignupService
    @Mock
    lateinit var metafileRepository: MetafileRepository
    @Mock
    lateinit var oktaService: OktaService
    @Mock
    lateinit var mailService: EmailService
    @Mock
    lateinit var smsService: SmsService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(service)
        Mockito.reset(metafileRepository)
        Mockito.reset(oktaService)
        Mockito.reset(mailService)
        Mockito.reset(smsService)
    }

    @Test
    fun `should do a signup`() {
        // Arrange
        val handler = SignupHandler(service, metafileRepository, oktaService, mailService, smsService)

        // Act
        val result = handler.generateCode()

        // Arrange
        Assertions.assertEquals(6, result.length)
    }

}