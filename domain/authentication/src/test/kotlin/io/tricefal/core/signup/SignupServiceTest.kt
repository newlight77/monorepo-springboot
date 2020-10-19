package io.tricefal.core.signup

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
class SignupServiceTest {

    @Mock
    lateinit var adapter: ISignupAdapter

    lateinit var service: ISignupService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should do a signup`() {
        // Arranges
        val notification = SignupNotificationDomain.Builder("kong")
                .smsFrom("smsFrom")
                .smsTo("smsTo")
                .smsContent("smsContent")
                .emailFrom("emailFrom")
                .emailTo("emailTo")
                .emailSubject("emailSubject")
                .emailGreeting("emailGreeting")
                .emailContent("emailContent")
                .build()
        val signup = SignupDomain.Builder("kong@gmail.com")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
//                .notification(notification)
                .build()

        Mockito.`when`(adapter.signup(signup)).thenReturn(signup)
        Mockito.`when`(adapter.register(signup)).thenReturn(true)
        Mockito.`when`(adapter.sendEmail(notification)).thenReturn(true)
        Mockito.`when`(adapter.sendSms(notification)).thenReturn(true)

        service = SignupService(adapter)

        // Act
        val result = service.signup(signup, notification)

        // Arrange
        Mockito.verify(adapter).signup(signup)
        Assertions.assertTrue(result.registered!!)
        Assertions.assertTrue(result.emailSent!!)
        Assertions.assertTrue(result.activationCodeSent!!)
    }

    @Test
    fun `should find a signup by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val signup = SignupDomain.Builder(username)
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

        Mockito.`when`(adapter.findByUsername(username)).thenReturn(Optional.of(signup))

        service = SignupService(adapter)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(signup, result)
    }

    @Test
    fun `should update the signup status`() {
        // Arrange
        val username = "kong@gmail.com"

        val state = SignupStateDomain.Builder("kong")
                .statusUpdated(true)
                .build()

        val signup = SignupDomain.Builder(username)
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .state(state)
                .build()

        Mockito.`when`(adapter.updateStatus(signup)).thenReturn(signup)

        service = SignupService(adapter)

        // Act
        val result = service.updateStatus(signup, Status.EMPLOYEE)

        // Arrange
        Mockito.verify(adapter).updateStatus(signup)
        Assertions.assertTrue(result.statusUpdated!!)
    }
}