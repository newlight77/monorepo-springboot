package io.tricefal.core.signup

import io.tricefal.core.notification.NotificationDomain
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

    lateinit var service: SignupService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should do a signup`() {
        // Arranges
        val notification = NotificationDomain.Builder("kong")
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

        Mockito.`when`(adapter.save(signup)).thenReturn(signup)
        Mockito.`when`(adapter.register(signup)).thenReturn(true)
        Mockito.`when`(adapter.sendEmail(notification)).thenReturn(true)
        Mockito.`when`(adapter.sendSms(notification)).thenReturn(true)

        service = SignupService(adapter)

        // Act
        val result = service.signup(signup, notification)

        // Arrange
        Mockito.verify(adapter).save(signup)
        Assertions.assertTrue(result.registered!!)
        Assertions.assertTrue(result.emailSent!!)
        Assertions.assertTrue(result.smsSent!!)
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

    @Test
    fun `should delete the signup`() {

    }

    @Test
    fun `should find all signups`() {

    }

    @Test
    fun `should activate the signup`() {

    }

    @Test
    fun `should deactivate the signup`() {

    }

    @Test
    fun `should resend code for a signup`() {

    }

    @Test
    fun `should verify by code for a signup`() {

    }

    @Test
    fun `should verify by email for a signup`() {

    }

    @Test
    fun `should update a signup upon portrait uploaded`() {

    }

    @Test
    fun `should update a signup upon resume uploaded`() {

    }

    @Test
    fun `should update a signup upon resume linkedin uploaded`() {

    }

    @Test
    fun `should generate an activation code with 6 digits`() {
        // Arrange

        // Act
        val result = service.generateCode()

        // Arrange
        Assertions.assertEquals(6, result.length)
    }

    @Test
    fun `should encode and decode a string`() {
        // Arrange
        val code = "123456"

        // Act
        val encoded = service.encode(code)
        val decoded = service.decode(encoded)

        // Arrange
        Assertions.assertEquals("123456", decoded)
    }
}