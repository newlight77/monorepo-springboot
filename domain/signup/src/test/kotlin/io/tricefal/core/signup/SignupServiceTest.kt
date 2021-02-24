package io.tricefal.core.signup

import com.nhaarman.mockitokotlin2.eq
import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.metafile.Representation
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
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
    lateinit var dataAdapter: SignupDataAdapter

    lateinit var service: SignupService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(dataAdapter)
        service = SignupService(dataAdapter)
    }

    @Test
    fun `should do a signup`() {
        // Arranges
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl",
            emailFrom = "emailFrom", smsFrom = "smsFrom",
            emailAdmin = "adminEmail", smsAdminNumber = "adminNumber")
        val signup = SignupDomain.Builder("kong@gmail.com")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .state(SignupStateDomain.Builder("kong@gmail.com").build())
                .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
                Optional.empty(),
                Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.save(signup)).thenReturn(signup)
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.register(signup)).thenReturn(true)
        Mockito.`when`(dataAdapter.sendEmail(any(EmailNotificationDomain::class.java))).thenReturn(true)
        Mockito.`when`(dataAdapter.sendSms(eq("kong@gmail.com"), any(SmsNotificationDomain::class.java))).thenReturn(true)

        // Act
        val result = service.signup(signup, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).save(signup)
        Assertions.assertTrue(result.saved!!)
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

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(signup))

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(signup, result)
    }

    @Test
    fun `should update the signup status`() {
        // Arrange
        val username = "kong@gmail.com"
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl",
            emailFrom = "emailFrom", smsFrom = "smsFrom",
            emailAdmin = "adminEmail", smsAdminNumber = "adminNumber")

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

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.sendEmail(any(EmailNotificationDomain::class.java))).thenReturn(true)

        // Act
        val result = service.updateStatus(signup, Status.EMPLOYEE, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).update(signup)
        Assertions.assertTrue(result.statusUpdated!!)
    }

    @Test
    fun `should update the signup state upon company completion`() {
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

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        // Act
        val result = service.companyCompleted(username)

        // Arrange
        Mockito.verify(dataAdapter).update(signup)
        Assertions.assertTrue(result.completed!!)
    }

    @Test
    fun `should do a hard delete the signup when the code matches the activation code`() {
        // Arrange
        val username = "kong@gmail.com"

        val state = SignupStateDomain.Builder("kong")
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

        Mockito.doNothing().`when`(dataAdapter).delete(signup.username)

        val code = "123456"

        // Act
        service.delete(signup, code)

        // Arrange
        Mockito.verify(dataAdapter).delete("kong@gmail.com")

    }

    @Test
    fun `should do a soft delete the signup when the code does not match the activation code`() {
        // Arrange
        val username = "kong@gmail.com"

        val state = SignupStateDomain.Builder("kong")
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

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        val code = ""

        // Act
        service.delete(signup, code)

        // Arrange
        Assertions.assertTrue(state.deleted!!)

    }

    @Test
    fun `should find all signups`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = SignupStateDomain.Builder("kong")
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

        Mockito.`when`(dataAdapter.findAll()).thenReturn(listOf(signup))

        // Act
        val result = service.findAll()

        // Assert
        Assertions.assertEquals(1, result.size)
    }

    @Test
    fun `should activate the signup`() {
        // Arrange
        val username = "kong@gmail.com"

        val state = SignupStateDomain.Builder("kong")
            .build()
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl",
            emailFrom = "emailFrom", smsFrom = "smsFrom",
            emailAdmin = "adminEmail", smsAdminNumber = "adminNumber")

        val signup = SignupDomain.Builder(username)
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("123456")
            .status(Status.FREELANCE)
            .state(state)
            .build()

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.sendEmail(any(EmailNotificationDomain::class.java))).thenReturn(true)

        // Act
        val result = service.activate(signup, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendEmail(any(EmailNotificationDomain::class.java))
        Assertions.assertTrue(result.validated!!)
    }

    @Test
    fun `should deactivate the signup`() {
// Arrange
        val username = "kong@gmail.com"

        val state = SignupStateDomain.Builder("kong")
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

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        // Act
        val result = service.deactivate(signup)

        // Arrange
        Assertions.assertFalse(result.validated!!)
    }

    @Test
    fun `should resend activation code sms for a validation`() {
        // Arranges
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("123456")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
            Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.sendSms(eq("kong@gmail.com"), any(SmsNotificationDomain::class.java))).thenReturn(true)

        // Act
        val result = service.resendCodeBySmsForValidation(signup, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendSms(eq("kong@gmail.com"), any(SmsNotificationDomain::class.java))
        Assertions.assertTrue(result)
    }

    @Test
    fun `should resend activation code by email for a validation`() {
        // Arranges
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("123456")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
            Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.sendEmail(any(EmailNotificationDomain::class.java))).thenReturn(true)

        // Act
        val result = service.resendCodeByEmailForValidation(signup, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendEmail(any(EmailNotificationDomain::class.java))
        Assertions.assertTrue(result)
    }

    @Test
    fun `should resend activation code by email and for a validation when activation code is not present`() {
        // Arranges
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
            Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.`when`(dataAdapter.sendEmail(any(EmailNotificationDomain::class.java))).thenReturn(true)
        Mockito.`when`(dataAdapter.sendSms(eq("kong@gmail.com"), any(SmsNotificationDomain::class.java))).thenReturn(true)

        // Act
        val result = service.resendCodeByEmailForValidation(signup, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendEmail(any(EmailNotificationDomain::class.java))
        Mockito.verify(dataAdapter).sendSms(eq("kong@gmail.com"), any(SmsNotificationDomain::class.java))
        Assertions.assertTrue(result)
    }

    @Test
    fun `should verify by code for a signup`() {
        // Arranges
        val code = "123456"
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("123456")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        // Act
        val result = service.verifyByCode(signup, code)

        // Arrange
        Assertions.assertTrue(result.smsValidated!!)
    }

    @Test
    fun `should verify by email for a signup`() {
        // Arranges
        val token = "MTIwODE1.a29uZ0BnbWFpbC5jb20=.61EVWjtwW0L1"
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("120815")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
            Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        // Act
        val result = service.verifyByCodeFromToken(token)

        // Arrange
        Assertions.assertTrue(result.emailValidated!!)
    }

    @Test
    fun `should update a signup upon resume uploaded`() {
        // Arranges
        val username = "kong@gmail.com"
        val signup = SignupDomain.Builder(username)
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("120815")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder(username).build())
            .build()
        val metafileDomain = MetafileDomain(username, "filename", Representation.PORTRAIT, "pdf", 1024, Instant.now())

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.doNothing().`when`(dataAdapter).resumeUploaded(username, "filename")

        // Act
        val result = service.resumeUploaded(signup, metafileDomain)

        // Arrange
        Assertions.assertTrue(result.resumeUploaded!!)
    }

    @Test
    fun `should update a signup upon resume linkedin uploaded`() {
        // Arranges
        val username = "kong@gmail.com"
        val signup = SignupDomain.Builder(username)
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("120815")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder(username).build())
            .build()
        val metafileDomain = MetafileDomain(username, "filename", Representation.PORTRAIT, "pdf", 1024, Instant.now())

        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))
        Mockito.doNothing().`when`(dataAdapter).resumeLinkedinUploaded(username, "filename")

        // Act
        val result = service.resumeLinkedinUploaded(signup, metafileDomain)

        // Arrange
        Assertions.assertTrue(result.resumeLinkedinUploaded!!)
    }

    @Test
    fun `should update a signup upon profile resume uploaded`() {
        // Arranges
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("120815")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
            Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        // Act
        val result = service.profileResumeUploaded(signup.username, "filename")

        // Arrange
        Assertions.assertEquals(true, result.state!!.resumeUploaded)
        Assertions.assertEquals("filename", result.resumeFilename!!)
    }

    @Test
    fun `should update a signup upon profile resume linkedin uploaded`() {
        // Arranges
        val signup = SignupDomain.Builder("kong@gmail.com")
            .firstname("kong")
            .lastname("to")
            .phoneNumber("1234567890")
            .signupDate(Instant.now())
            .activationCode("120815")
            .status(Status.FREELANCE)
            .state(SignupStateDomain.Builder("kong@gmail.com").build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(
            Optional.of(signup)
        )
        Mockito.`when`(dataAdapter.update(signup)).thenReturn(Optional.of(signup))

        // Act
        val result = service.profileResumeLinkedinUploaded(signup.username, "filename")

        // Arrange
        Assertions.assertEquals(true, result.state!!.resumeLinkedinUploaded)
        Assertions.assertEquals("filename", result.resumeLinkedinFilename!!)
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


    @Test
    fun `should generate a random string`() {
        // Arrange

        // Act
        val result = service.randomString()

        // Arrange
        Assertions.assertEquals(12, result.length)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}