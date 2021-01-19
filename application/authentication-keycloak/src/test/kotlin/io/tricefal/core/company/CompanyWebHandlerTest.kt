package io.tricefal.core.company

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import io.tricefal.core.InfrastructureMockBeans
import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.freelance.*
import io.tricefal.core.metafile.MetafileJpaRepository
import io.tricefal.core.metafile.MetafileService
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
import java.io.File

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
@DataJpaTest
@ContextConfiguration(classes = [InfrastructureMockBeans::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyWebHandlerTest {
    @Autowired
    lateinit var webHandler: CompanyWebHandler

    @Autowired
    lateinit var metafileService: MetafileService

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var companyService: ICompanyService

    @MockBean
    lateinit var jpaRepository: CompanyJpaRepository

    @MockBean
    lateinit var metaFileRepository: MetafileJpaRepository

    @MockBean
    lateinit var eventPublisher: CompanyEventPublisher

    @TempDir
    lateinit var tempDir: File

    @Captor
    var companyCaptor: ArgumentCaptor<CompanyEntity> = ArgumentCaptor.forClass(CompanyEntity::class.java)

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
    fun `should create a company successfully`() {
        // Arrange
        val email = "kong@gmail.com"
        val companyName = "name"
        val contact = ContactModel.Builder(email = email).build()
        val address = AddressModel.Builder().build()
//        val bankInfo = BankInfoModel.Builder().build()
        val state = CompanyStateModel.Builder(companyName).build()
        val company = CompanyModel.Builder(companyName)
            .adminContact(contact)
            .fiscalAddress(address)
//            .bankInfo(bankInfo)
            .state(state)
            .build()
        val companyEntity = toEntity(fromModel(company))

        Mockito.`when`(jpaRepository.save(any(CompanyEntity::class.java))).thenReturn(companyEntity)
        Mockito.`when`(jpaRepository.findByName(companyName)).thenReturn(
//                listOf(),
                listOf(companyEntity))

        // Act
        val result = webHandler.create(company)

        // Arrange
        Assertions.assertEquals("name", result.raisonSocial)
    }

    @Test
    fun `should find a company by username`() {
        // Arrange
        val email = "kong@gmail.com"
        val companyName = "name"
        val contact = ContactModel.Builder(email = email).build()
        val address = AddressModel.Builder().build()
//        val bankInfo = BankInfoModel.Builder().build()
        val state = CompanyStateModel.Builder(companyName).build()
        val company = CompanyModel.Builder(companyName)
            .adminContact(contact)
            .fiscalAddress(address)
//            .bankInfo(bankInfo)
            .state(state)
            .build()
        val companyEntity = toEntity(fromModel(company))


        Mockito.`when`(jpaRepository.findByName(companyName)).thenReturn(listOf(companyEntity))

        // Act
        val result = webHandler.findByName("name")

        // Arrange
        Assertions.assertEquals("name", result.raisonSocial)
    }

    @Test
    fun `should patch the company availability`() {
        // Arrange
        val email = "kong@gmail.com"
        val companyName = "name"
        val contact = ContactModel.Builder(email = email).build()
        val address = AddressModel.Builder().build()
//        val bankInfo = BankInfoModel.Builder().build()
        val state = CompanyStateModel.Builder(companyName).build()
        val company = CompanyModel.Builder(companyName)
            .adminContact(contact)
            .fiscalAddress(address)
//            .bankInfo(bankInfo)
            .state(state)
            .build()
        val companyEntity = toEntity(fromModel(company))

        Mockito.`when`(jpaRepository.findByName(companyName)).thenReturn(listOf(companyEntity, companyEntity))
        Mockito.`when`(jpaRepository.save(any(CompanyEntity::class.java))).thenReturn(companyEntity)

        val ops = listOf(
            PatchOperation.Builder("replace").path("/raisonSocial").value("new raison").build(),
            PatchOperation.Builder("replace").path("/nomCommercial").value("new nom commercial").build(),
            PatchOperation.Builder("replace").path("/adminContact/lastName").value("new name").build()
        )

        // Act
        val result = webHandler.patch(companyName, ops)

        // Arrange
        Assertions.assertEquals("new raison", result.raisonSocial)
        Assertions.assertEquals("new nom commercial", result.nomCommercial)
        Assertions.assertEquals("new name", result.adminContact?.lastName)
    }

    @Test
    fun `should send notification upon company form completion`() {
        // Arrange
        val email = "kong@gmail.com"
        val companyName = "name"
        val contact = ContactModel.Builder(email = email).build()
        val address = AddressModel.Builder().build()
//        val bankInfo = BankInfoModel.Builder().build()
        val state = CompanyStateModel.Builder(companyName).build()
        val company = CompanyModel.Builder(companyName)
            .adminContact(contact)
            .fiscalAddress(address)
//            .bankInfo(bankInfo)
            .state(state)
            .build()
        val companyEntity = toEntity(fromModel(company))


        Mockito.`when`(jpaRepository.findByName(companyName)).thenReturn(listOf(companyEntity))
        Mockito.`when`(jpaRepository.save(any(CompanyEntity::class.java))).thenReturn(companyEntity)
        Mockito.`when`(emailService.send(any(EmailMessage::class.java))).thenReturn(true)
        Mockito.doNothing().`when`(eventPublisher).publishCompanyCompletedEvent(companyName)

        // Act
        webHandler.completed(companyName)

        // Arrange
        Mockito.verify(eventPublisher).publishCompanyCompletedEvent(companyName)
        Mockito.verify(emailService).send(any(EmailMessage::class.java))
//        Assertions.assertEquals(1, greenMail.receivedMessages.size)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

}