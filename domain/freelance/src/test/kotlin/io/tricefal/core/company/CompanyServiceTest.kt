package io.tricefal.core.company

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import io.tricefal.core.freelance.*
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.PatchOperation
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
class CompanyServiceTest {

    @Mock
    lateinit var dataAdapter: CompanyDataAdapter

    lateinit var service: ICompanyService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(dataAdapter)
    }

    @Test
    fun `should do a create`() {
        // Arranges
        val company = CompanyDomain.Builder("company name")
                .build()

        Mockito.`when`(dataAdapter.findByName("company name")).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(company)).thenReturn(company)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.create(company)

        // Arrange
        Mockito.verify(dataAdapter).create(company)
        Assertions.assertEquals(company.companyName, result.companyName)
    }

    @Test
    fun `should find a company by companyName`() {
        // Arrange
        val companyName = "company name"
        val company = CompanyDomain.Builder(companyName)
                .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))

        service = CompanyService(dataAdapter)

        // Act
        val result = service.findByName(companyName)

        // Arrange
        Assertions.assertEquals(company.companyName, result.companyName)
    }


    @Test
    fun `should patch the company name with new name`() {
        // Arrange
        val companyName = "company name"
        val date = Instant.now()

        val company = CompanyDomain.Builder(companyName)
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))

        val transientDomain = CompanyDomain.Builder(companyName)
            .adminContact(ContactDomain.Builder().build())
            .bankInfo(BankInfoDomain.Builder().build())
            .fiscalAddress(AddressDomain.Builder(companyName).build())
            .state(CompanyStateDomain.Builder(companyName).build())
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.update(any(CompanyDomain::class.java))).thenReturn(transientDomain)

        val ops = listOf(PatchOperation.Builder("replace").path("/companyName").value("new name").build())

        service = CompanyService(dataAdapter)

        // Act
        val result = service.patch(companyName, ops)

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.companyName).isEqualTo("new name")
    }

    @Test
    fun `should patch a company domain with a non existing child bank info`() {
        // Arrange
        val companyName = "company name"
        val date = Instant.now()

        val company = CompanyDomain.Builder(companyName)
            .lastDate(date)
            .build()
        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))

        val transientDomain = CompanyDomain.Builder(companyName)
            .adminContact(ContactDomain.Builder().build())
            .bankInfo(BankInfoDomain.Builder().build())
            .fiscalAddress(AddressDomain.Builder(companyName).build())
            .state(CompanyStateDomain.Builder(companyName).build())
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.update(any(CompanyDomain::class.java))).thenReturn(transientDomain)

        val ops = listOf(PatchOperation.Builder("replace").path("/bankInfo/iban").value("FR01009940913").build())

        service = CompanyService(dataAdapter)

        // Act
        val result = service.patch(companyName, ops)

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.bankInfo?.iban).isEqualTo("FR01009940913")
    }

    @Test
    fun `should update company upon kbis uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-kbis.pdf"
        val company = CompanyDomain.Builder(companyName)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))
        Mockito.`when`(dataAdapter.update(company)).thenReturn(company)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnKbisUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.kbisFilename, "new-kbis.pdf")
    }

    @Test
    fun `should create a new company upon kbis uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-kbis.pdf"

        val newCompany = CompanyDomain.Builder(companyName)
            .kbisFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newCompany)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnKbisUploaded(companyName, "new-kbis.pdf")

        // Arrange
        Assertions.assertEquals(result.companyName, companyName)
        Assertions.assertEquals(result.kbisFilename, filename)
    }

    @Test
    fun `should update company upon rib uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-rib.pdf"
        val company = CompanyDomain.Builder(companyName)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))
        Mockito.`when`(dataAdapter.update(company)).thenReturn(company)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnRibUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.ribFilename, "new-rib.pdf")
    }

    @Test
    fun `should create a new company upon rib uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-rib.pdf"

        val newCompany = CompanyDomain.Builder(companyName)
            .ribFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newCompany)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnRibUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.companyName, companyName)
        Assertions.assertEquals(result.ribFilename, "new-rib.pdf")
    }

    @Test
    fun `should update company upon rc uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-rc.pdf"
        val company = CompanyDomain.Builder(companyName)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))
        Mockito.`when`(dataAdapter.update(company)).thenReturn(company)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnRcUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.rcFilename, "new-rc.pdf")
    }

    @Test
    fun `should create a new company upon rc uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-rc.pdf"

        val newCompany = CompanyDomain.Builder(companyName)
            .rcFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newCompany)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnRcUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.companyName, companyName)
        Assertions.assertEquals(result.rcFilename, "new-rc.pdf")
    }

    @Test
    fun `should update company upon urssaf uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-urssaf.pdf"
        val company = CompanyDomain.Builder(companyName)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))
        Mockito.`when`(dataAdapter.update(company)).thenReturn(company)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnUrssafUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.urssafFilename, "new-urssaf.pdf")
    }

    @Test
    fun `should create a new company upon urssaf uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-urssaf.pdf"

        val newCompany = CompanyDomain.Builder(companyName)
            .urssafFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newCompany)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnUrssafUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.companyName, companyName)
        Assertions.assertEquals(result.urssafFilename, "new-urssaf.pdf")
    }

    @Test
    fun `should update company upon fiscal uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-fiscal.pdf"
        val company = CompanyDomain.Builder(companyName)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))
        Mockito.`when`(dataAdapter.update(company)).thenReturn(company)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnFiscalUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.fiscalFilename, "new-fiscal.pdf")
    }

    @Test
    fun `should create a new company upon fiscal uploaded`() {
        // Arrange
        val companyName = "company name"
        val filename = "new-fiscal.pdf"

        val newCompany = CompanyDomain.Builder(companyName)
            .fiscalFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newCompany)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.updateOnFiscalUploaded(companyName, filename)

        // Arrange
        Assertions.assertEquals(result.companyName, companyName)
        Assertions.assertEquals(result.fiscalFilename, "new-fiscal.pdf")
    }


    @Test
    fun `should send a notification by email upon company completion`() {
        // Arranges

        val companyName = "company name"
        val company = CompanyDomain.Builder(companyName)
            .state(CompanyStateDomain(companyName))
            .build()
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl",
            emailFrom = "emailFrom", smsFrom = "smsFrom",
            emailAdmin = "adminEmail", smsAdminNumber = "adminNumber")


        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(company))
        Mockito.`when`(dataAdapter.update(company)).thenReturn(company)
        Mockito.`when`(dataAdapter.sendEmail(eq("company name"), any(EmailNotificationDomain::class.java))).thenReturn(true)

        service = CompanyService(dataAdapter)

        // Act
        val result = service.completed(companyName, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendEmail(eq("company name"), any(EmailNotificationDomain::class.java))
        Mockito.verify(dataAdapter).companyCompleted(companyName)
        Assertions.assertTrue(result.state?.completed!!)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}