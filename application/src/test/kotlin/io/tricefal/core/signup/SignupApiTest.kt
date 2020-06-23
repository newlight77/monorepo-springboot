package io.tricefal.core.signup

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import io.tricefal.core.login.SignupJpaRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@ComponentScan("io.tricefal.core")
@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SignupApiTest {
    @Autowired
    lateinit var service: SignupHandler

    @MockBean
    lateinit var repository: SignupJpaRepository

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

//    @Test
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
                .activationCode("123456")
                .status(Status.FREELANCE)
                .state(state)
                .build()

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.save(signupEntity)).thenReturn(signupEntity)

        // Act
        val result = service.signup(signup)

        // Arrange
        Mockito.verify(repository).save(signupEntity)
        Assertions.assertTrue(result.oktaRegistered!!)
        Assertions.assertTrue(result.emailSent!!)
        Assertions.assertTrue(result.activationCodeSent!!)
    }

    @Test
    fun `should find a signup by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = SignupStateModel.Builder(username)
                .build()
//        val notification = SignupNotificationModel.Builder(username)
//                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
//                .notification(notification)
                .state(state)
                .build()

        val expected = SignupModel.Builder(username)
                .password("")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(signup.signupDate)
                .activationCode("123456")
                .status(Status.FREELANCE)
//                .notification(notification)
                .state(state)
                .build()

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signupEntity))

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertTrue(result.isPresent)
        Assertions.assertEquals(expected.username, result.get().username)
        Assertions.assertEquals(null, result.get().password)
    }

//    @Test
    fun `should upload a file for the profile`() {
        // Arrange
        val username = "kong@gmail.com"
        val multipart = Mockito.mock(MultipartFile::class.java)
        Mockito.`when`(multipart.originalFilename).thenReturn("test-filename")
        Mockito.`when`(multipart.contentType).thenReturn("txt")
        Mockito.`when`(multipart.inputStream).thenReturn(ByteArrayInputStream("testing data".toByteArray()))

        // Act
        val result = service.uploadResume(username, multipart)

        // Arrange
        Assertions.assertTrue(result.resumeUploaded!!)
    }

//    @Test
    fun `should activate the account by code`() {
        // Arrange
        val username = "kong@gmail.com"
        val code = "123456"
        val state = SignupStateModel.Builder(username)
                .build()
//        val notification = SignupNotificationModel.Builder(username)
//                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode(code)
                .status(Status.FREELANCE)
//                .notification(notification)
                .state(state)
                .build()
        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signupEntity))
        Mockito.`when`(repository.save(signupEntity)).thenReturn(signupEntity)

        // Act
        val result = service.activate(username, code)

        // Arrange
        Assertions.assertTrue(result.activatedByCode!!)
    }

//    @Test
    fun `should update the signup status`() {
        // Arrange
        val username ="kong@gmail.com"
        val state = SignupStateModel.Builder(username)
                .build()
//        val notification = SignupNotificationModel.Builder(username)
//                .build()
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
//                .notification(notification)
                .state(state)
                .build()

        val expected = SignupEntity( null, username, "kong", "to",
                "1234567890", "123456", Status.EMPLOYEE.toString(), signup.signupDate)

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signupEntity))
        Mockito.`when`(repository.save(signupEntity)).thenReturn(expected)

        // Act
        val result = service.updateStatus(username, Status.EMPLOYEE.toString())

        // Arrange
        Mockito.verify(repository).save(expected)
        Assertions.assertTrue(result.statusUpdated!!)
//        Assertions.assertEquals(Status.EMPLOYEE.toString(), state.statusUpdated)
    }
}