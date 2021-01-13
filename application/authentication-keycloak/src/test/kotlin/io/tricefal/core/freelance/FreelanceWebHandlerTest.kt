package io.tricefal.core.freelance

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import io.tricefal.core.InfrastructureMockBeans
import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.metafile.*
import io.tricefal.shared.util.json.PatchOperation
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.absoluteValue

//@ExtendWith(SpringExtension::class, MockitoExtension::class)
@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
@DataJpaTest
@ContextConfiguration(classes = [InfrastructureMockBeans::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FreelanceWebHandlerTest {
    @Autowired
    lateinit var webHandler: FreelanceWebHandler

    @Autowired
    lateinit var metafileService: MetafileService

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var freelanceService: IFreelanceService

    @MockBean
    lateinit var jpaRepository: FreelanceJpaRepository

    @MockBean
    lateinit var metaFileRepository: MetafileJpaRepository

    @MockBean
    lateinit var eventPublisher: FreelanceEventPublisher

    @TempDir
    lateinit var tempDir: File

    @Captor
    var freelanceCaptor: ArgumentCaptor<FreelanceEntity> = ArgumentCaptor.forClass(FreelanceEntity::class.java)

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
    fun `should create a freelance successfully`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = FreelanceStateModel.Builder(username)
                .build()
        val company = CompanyModel.Builder(raisonSocial = "raison social").build()
        val contact = ContactModel.Builder(email = username).build()
        val privacyDetail = PrivacyDetailModel.Builder(username = username).build()
        val freelance = FreelanceModel.Builder(username)
                .company(company)
                .contact(contact)
                .privacyDetail(privacyDetail)
                .availability(Availability.AVAILABLE)
                .state(state)
                .build()

        val freelanceEntity = toEntity(fromModel(freelance))
        Mockito.`when`(jpaRepository.save(any(FreelanceEntity::class.java))).thenReturn(freelanceEntity)
        Mockito.`when`(jpaRepository.findByUsername("kong@gmail.com")).thenReturn(
//                listOf(),
//                listOf(),
//                listOf(),
                listOf(freelanceEntity))

        // Act
        val result = webHandler.create(freelance)

        // Arrange
        Assertions.assertEquals(Availability.AVAILABLE, result.availability)
    }

    @Test
    fun `should find a freelance by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = FreelanceStateModel.Builder(username)
            .build()
        val company = CompanyModel.Builder(raisonSocial = "raison social").build()
        val contact = ContactModel.Builder(email = username).build()
        val privacyDetail = PrivacyDetailModel.Builder(username = username).build()
        val freelance = FreelanceModel.Builder(username)
            .company(company)
            .contact(contact)
            .privacyDetail(privacyDetail)
            .availability(Availability.AVAILABLE)
            .state(state)
            .build()
        val freelanceEntity = toEntity(fromModel(freelance))

        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(listOf(freelanceEntity))

        // Act
        val result = webHandler.findByUsername(username)

        // Arrange
        Assertions.assertEquals(username, result.username)
    }

    @Test
    fun `should upload a kbis file for the freelance`() {
        // Arrange
        val username = "kong@gmail.com"
        val state = FreelanceStateModel.Builder(username)
                .build()
        val company = CompanyModel.Builder(raisonSocial = "raison social").build()
        val contact = ContactModel.Builder(email = username).build()
        val privacyDetail = PrivacyDetailModel.Builder(username = username).build()
        val freelance = FreelanceModel.Builder(username)
            .company(company)
            .contact(contact)
            .privacyDetail(privacyDetail)
            .availability(Availability.AVAILABLE)
            .state(state)
            .build()
        val freelanceEntity = toEntity(fromModel(freelance))

        val multipart = Mockito.mock(MultipartFile::class.java)
        val filename = "${tempDir.absolutePath}/${username}-test-file-" + Random().nextInt().absoluteValue + ".txt"
        Mockito.`when`(multipart.originalFilename).thenReturn(filename)
        Mockito.`when`(multipart.contentType).thenReturn("txt")
        Mockito.`when`(multipart.inputStream).thenReturn(ByteArrayInputStream("testing data".toByteArray()))
        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(listOf(freelanceEntity))
        freelanceEntity.state?.kbisUploaded = true
        Mockito.`when`(jpaRepository.save(any(FreelanceEntity::class.java))).thenReturn(freelanceEntity)
        val metafileEntity = toEntity(
                fromModel(
                        MetafileModel.Builder(username, filename, Representation.PORTRAIT)
                            .contentType("application/octet-stream")
                            .size(12345)
                            .build()))
        Mockito.`when`(metaFileRepository.save(any(MetafileEntity::class.java))).thenReturn(metafileEntity)

        // Act
        val result = webHandler.uploadKbis(username, multipart)

        // Arrange
        Assertions.assertTrue(result.state?.kbisUploaded!!)
        Assertions.assertTrue(Files.exists(Paths.get(filename)))
        Files.deleteIfExists(Paths.get(filename))
    }

    @Test
    fun `should patch the freelance availability`() {
        // Arrange
        val username ="kong@gmail.com"
        val state = FreelanceStateModel.Builder(username)
            .build()
        val company = CompanyModel.Builder(raisonSocial = "raison social").build()
        val contact = ContactModel.Builder(email = username).build()
        val privacyDetail = PrivacyDetailModel.Builder(username = username).build()
        val freelance = FreelanceModel.Builder(username)
            .company(company)
            .contact(contact)
            .privacyDetail(privacyDetail)
            .availability(Availability.AVAILABLE)
            .state(state)
            .build()
        val freelanceEntity = toEntity(fromModel(freelance))

        val companyEntity = CompanyEntity(0, raisonSocial = "new name")
        val contactEntity = ContactEntity(0, email = username)
        val privacyDetailEntity = PrivacyDetailEntity(0, username)
        val expected = FreelanceEntity( null, username, contactEntity, companyEntity,
                privacyDetailEntity, state=toEntity(fromModel(state)), availability=Availability.IN_MISSION.toString())

        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(listOf(freelanceEntity))
        Mockito.`when`(jpaRepository.save(any(FreelanceEntity::class.java))).thenReturn(expected)

        val ops = listOf(
            PatchOperation.Builder("replace").path("/availability").value(Availability.IN_MISSION.toString()).build(),
            PatchOperation.Builder("replace").path("/company/raisonSocial").value("new name").build()
        )

        // Act
        val result = webHandler.patch(username, ops)

        // Arrange
        Assertions.assertEquals("IN_MISSION", result.availability.toString())
        Assertions.assertEquals("new name", result.company?.raisonSocial)
    }

    @Test
    fun `should send notification upon company form completion`() {
        // Arrange
        val username ="kong@gmail.com"
        val state = FreelanceStateModel.Builder(username).build()
        val company = CompanyModel.Builder(raisonSocial = "raison social").build()
        val contact = ContactModel.Builder(email = username).build()
        val privacyDetail = PrivacyDetailModel.Builder(username = username).build()
        val freelance = FreelanceModel.Builder(username)
            .company(company)
            .contact(contact)
            .privacyDetail(privacyDetail)
            .availability(Availability.AVAILABLE)
            .state(state)
            .build()
        val freelanceEntity = toEntity(fromModel(freelance))

        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(listOf(freelanceEntity))
        Mockito.`when`(jpaRepository.save(any(FreelanceEntity::class.java))).thenReturn(freelanceEntity)
        Mockito.`when`(emailService.send(any(EmailMessage::class.java))).thenReturn(true)
        Mockito.doNothing().`when`(eventPublisher).publishCompanyCompletedEvent(username)

        // Act
        webHandler.completed(username)

        // Arrange
        Mockito.verify(eventPublisher).publishCompanyCompletedEvent(username)
        Mockito.verify(emailService).send(any(EmailMessage::class.java))
//        Assertions.assertEquals(1, greenMail.receivedMessages.size)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

}