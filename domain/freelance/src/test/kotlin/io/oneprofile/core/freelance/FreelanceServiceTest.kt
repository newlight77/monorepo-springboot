package io.oneprofile.core.freelance

import com.nhaarman.mockitokotlin2.any
import io.oneprofile.core.company.CompanyDomain
import io.oneprofile.core.company.ContactDomain
import io.oneprofile.core.company.PrivacyDetailDomain
import io.oneprofile.core.notification.EmailNotificationDomain
import io.oneprofile.core.notification.MetaNotificationDomain
import io.oneprofile.shared.util.json.PatchOperation
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
class FreelanceServiceTest {

    @Mock
    lateinit var dataAdapter: FreelanceDataAdapter

    lateinit var service: IFreelanceService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(dataAdapter)
    }

    @Test
    fun `should do a create`() {
        // Arranges
        val freelance = FreelanceDomain.Builder("kong@gmail.com")
                .build()

        Mockito.`when`(dataAdapter.findByUsername("kong@gmail.com")).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(freelance)).thenReturn(freelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.create(freelance)

        // Arrange
        Mockito.verify(dataAdapter).create(freelance)
        Assertions.assertEquals(freelance.username, result.username)
    }

    @Test
    fun `should find a freelance by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
                .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(freelance.username, result.username)
    }


    @Test
    fun `should patch a freelance domain`() {
        // Arrange
        val username = "kong@gmail.com"
        val date = Instant.now()

        val freelance = FreelanceDomain.Builder(username)
            .company(CompanyDomain.Builder("company name").build())
            .contact(ContactDomain.Builder().build())
            .privacyDetail(PrivacyDetailDomain.Builder(username).build())
            .availability(Availability.AVAILABLE)
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))

        Mockito.`when`(dataAdapter.update(any(FreelanceDomain::class.java))).thenReturn(freelance)

        val ops = listOf(PatchOperation.Builder("replace").path("/availability").value(Availability.LOOKING).build())

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.patch(username, ops)

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.availability).isEqualTo(Availability.LOOKING)
    }

    @Test
    fun `should patch a freelance domain with a non existing child company`() {
        // Arrange
        val username = "kong@gmail.com"
        val date = Instant.now()

        val freelance = FreelanceDomain.Builder(username)
            .company(CompanyDomain.Builder("company name").build())
            .contact(ContactDomain.Builder().build())
            .privacyDetail(PrivacyDetailDomain.Builder(username).build())
            .availability(Availability.AVAILABLE)
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))

        Mockito.`when`(dataAdapter.update(any(FreelanceDomain::class.java))).thenReturn(freelance)

        val ops = listOf(
            PatchOperation.Builder("replace").path("/company/raisonSocial").value("new raisonSocial").build(),
            PatchOperation.Builder("replace").path("/company/nomCommercial").value("new nom commercial").build()
        )

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.patch(username, ops)

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.company?.raisonSocial).isEqualTo("new raisonSocial")
        org.assertj.core.api.Assertions.assertThat(result.company?.nomCommercial).isEqualTo("new nom commercial")
    }

    @Test
    fun `should update freelance upon kbis uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(freelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnKbisUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.kbisUploaded!!)
    }

    @Test
    fun `should create a new freelance if not existed upon kbis uploaded`() {
        // Arrange
        val username = "kong@gmail.com"

        val newFreelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnKbisUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
    }

    @Test
    fun `should update freelance upon rib uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(freelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRibUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.ribUploaded!!)
    }

    @Test
    fun `should create a new freelance if not existed upon rib uploaded`() {
        // Arrange
        val username = "kong@gmail.com"

        val newFreelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRibUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.ribUploaded!!)
    }

    @Test
    fun `should update freelance upon rc uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(freelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRcUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.rcUploaded!!)
    }

    @Test
    fun `should create a new freelance if not existed upon rc uploaded`() {
        // Arrange
        val username = "kong@gmail.com"

        val newFreelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRcUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.rcUploaded!!)
    }

    @Test
    fun `should update freelance upon urssaf uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(freelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnUrssafUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.urssafUploaded!!)
    }

    @Test
    fun `should create a new freelance if not existed  upon urssaf uploaded`() {
        // Arrange
        val username = "kong@gmail.com"

        val newFreelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnUrssafUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.urssafUploaded!!)
    }

    @Test
    fun `should update freelance upon fiscal uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(freelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnFiscalUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.fiscalUploaded!!)
    }

    @Test
    fun `should create a new freelance if not existed  upon fiscal uploaded`() {
        // Arrange
        val username = "kong@gmail.com"

        val newFreelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnFiscalUploaded(username, "filename", Instant.now())

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertTrue(result.state?.fiscalUploaded!!)
    }


    @Test
    fun `should send a notification by email upon company completion`() {
        // Arranges

        val username = "kong@gmail.com"
        val companyName = "companyName"
        val freelance = FreelanceDomain.Builder(username)
            .company(CompanyDomain.Builder(companyName).build())
            .state(FreelanceStateDomain(username))
            .build()
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl",
            emailFrom = "emailFrom", smsFrom = "smsFrom",
            emailAdmin = "adminEmail", smsAdminNumber = "adminNumber")


        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(freelance)
        Mockito.`when`(dataAdapter.sendEmail(any(EmailNotificationDomain::class.java))).thenReturn(true)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.completed(username, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendEmail(any(EmailNotificationDomain::class.java))
        Mockito.verify(dataAdapter).companyCompleted(username, companyName)
        Assertions.assertTrue(result.state?.completed!!)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}