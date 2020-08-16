package io.tricefal.core.signup

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import io.tricefal.core.InfrastructureMockBeans
import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.login.SignupJpaRepository
import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.metafile.MetafileRepository
import io.tricefal.core.okta.OktaService
import io.tricefal.core.twilio.SmsMessage
import io.tricefal.core.twilio.SmsService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.Instant
import java.util.*

//@ExtendWith(SpringExtension::class, MockitoExtension::class)
@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
@DataJpaTest
@ContextConfiguration(classes = [InfrastructureMockBeans::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SignupWebHandlerTest {
    @Autowired
    lateinit var signupWebHandler: SignupWebHandler

    @MockBean
    lateinit var signupJpaRepository: SignupJpaRepository

    @Autowired
    lateinit var oktaService: OktaService

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var smsService: SmsService

    @Autowired
    lateinit var metaFileRepository: MetafileRepository

    @Captor
    var signupCaptor: ArgumentCaptor<SignupEntity> = ArgumentCaptor.forClass(SignupEntity::class.java)

    val greenMail = GreenMail(ServerSetup(2525, null, "smtp"))

    @BeforeEach
    internal fun beforeEach() {
        greenMail.reset()
    }

    @BeforeAll
    internal fun before() {
        greenMail.start()
    }

    @AfterAll
    internal fun after() {
        greenMail.stop()
    }

    @Test
    fun `should do a signup successfully`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = SignupStateModel.Builder(username)
                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .status(Status.FREELANCE)
                .state(state)
                .build()

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(signupJpaRepository.save(any(SignupEntity::class.java))).thenReturn(signupEntity)
        Mockito.`when`(oktaService.register(any(SignupDomain::class.java))).thenReturn(true)
        Mockito.`when`(emailService.send(any(EmailMessage::class.java))).thenReturn(true)
        Mockito.`when`(smsService.send(any(SmsMessage::class.java))).thenReturn("SM235g4fwee4qf32gqg8g")

        // Act
        val result = signupWebHandler.signup(signup)

        // Arrange
        Assertions.assertTrue(result.registered!!)
        Assertions.assertTrue(result.emailSent!!)
        Assertions.assertTrue(result.activationCodeSent!!)
    }

    @Test
    fun `should find a signup by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = SignupStateModel.Builder(username)
                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .status(Status.FREELANCE)
                .state(state)
                .build()

        val expected = SignupModel.Builder(username)
                .password("")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(signup.signupDate)
                .status(Status.FREELANCE)
                .state(state)
                .build()

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(signupJpaRepository.findByUsername(username)).thenReturn(Optional.of(signupEntity))

        // Act
        val result = signupWebHandler.findByUsername(username)

        // Arrange
        Assertions.assertTrue(result.isPresent)
        Assertions.assertEquals(expected.username, result.get().username)
        Assertions.assertEquals(null, result.get().password)
    }

    @Test
    fun `should upload a file for the profile`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = SignupStateModel.Builder(username)
                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .status(Status.FREELANCE)
                .state(state)
                .build()
        val signupEntity = toEntity(fromModel(signup))
        val multipart = Mockito.mock(MultipartFile::class.java)
        Mockito.`when`(multipart.originalFilename).thenReturn("test-filename")
        Mockito.`when`(multipart.contentType).thenReturn("txt")
        Mockito.`when`(multipart.inputStream).thenReturn(ByteArrayInputStream("testing data".toByteArray()))
        Mockito.`when`(signupJpaRepository.findByUsername(username)).thenReturn(Optional.of(signupEntity))
        Mockito.`when`(signupJpaRepository.save(any(SignupEntity::class.java))).thenReturn(signupEntity)
        Mockito.doNothing().`when`(metaFileRepository).save(any(MetafileDomain::class.java), any(InputStream::class.java))

        // Act
        val result = signupWebHandler.uploadResume(username, multipart)

        // Arrange
        Assertions.assertTrue(result.resumeUploaded!!)
    }

    @Test
    fun `should activate the account by code`() {
        // Arrange
        val username = "kong@gmail.com"
        val code = "123456"
        val state = SignupStateModel.Builder(username)
                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .status(Status.FREELANCE)
                .state(state)
                .build()
        val domain = fromModel(signup)
        domain.activationCode = "123456"
        val signupEntity = toEntity(domain)
        Mockito.`when`(signupJpaRepository.findByUsername(username)).thenReturn(Optional.of(signupEntity))
        Mockito.`when`(signupJpaRepository.save(any(SignupEntity::class.java))).thenReturn(signupEntity)

        // Act
        val result = signupWebHandler.activate(username, code)

        // Arrange
        Assertions.assertTrue(result.activatedByCode!!)
    }

    @Test
    fun `should update the signup status`() {
        // Arrange
        val username ="kong@gmail.com"
        val state = SignupStateModel.Builder(username)
                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .status(Status.FREELANCE)
                .state(state)
                .build()

        val expected = SignupEntity( null, username, "kong", "to",
                "1234567890", "123456", "as3af3af34faf3",
                Status.EMPLOYEE.toString(), signup.signupDate, signupState=toEntity(fromModel(state)))

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(signupJpaRepository.findByUsername(username)).thenReturn(Optional.of(signupEntity))
        Mockito.`when`(signupJpaRepository.save(any(SignupEntity::class.java))).thenReturn(expected)

        // Act
        val result = signupWebHandler.updateStatus(username, Status.EMPLOYEE)

        // Arrange
        Assertions.assertTrue(result.statusUpdated!!)
//        Mockito.verify(repository).save(signupCaptor.capture())
//        Assertions.assertEquals(Status.EMPLOYEE.toString(), signupCaptor.value.status)
    }

    @Test
    fun `should generate an activation code with 6 digits`() {
        // Arrange

        // Act
        val result = signupWebHandler.generateCode()

        // Arrange
        Assertions.assertEquals(6, result.length)
    }

    @Test
    fun `should generate a random string`() {
        // Arrange

        // Act
        val result = signupWebHandler.randomString()

        // Arrange
        Assertions.assertEquals(12, result.length)
    }

    @Test
    fun `should encode and decode a string`() {
        // Arrange
        val code = "123456"

        // Act
        val encoded = signupWebHandler.encode(code)
        val decoded = signupWebHandler.decode(encoded)

        // Arrange
        Assertions.assertEquals("123456", decoded)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

}