package io.tricefal.core.freelance

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
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
        Assertions.assertEquals(freelance.username, result.get().username)
    }

    @Test
    fun `should update freelance upon kbis uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-kbis.pdf"
        val freelance = FreelanceDomain.Builder(username)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(Optional.of(freelance))

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnKbisUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.kbisFilename, "new-kbis.pdf")
    }

    @Test
    fun `should create a new freelance upon kbis uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-kbis.pdf"

        val newFreelance = FreelanceDomain.Builder(username)
            .kbisFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnKbisUploaded(username, "new-kbis.pdf")

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertEquals(result.kbisFilename, filename)
    }

    @Test
    fun `should update freelance upon rib uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-rib.pdf"
        val freelance = FreelanceDomain.Builder(username)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(Optional.of(freelance))

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRibUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.ribFilename, "new-rib.pdf")
    }

    @Test
    fun `should create a new freelance upon rib uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-rib.pdf"

        val newFreelance = FreelanceDomain.Builder(username)
            .ribFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRibUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertEquals(result.ribFilename, "new-rib.pdf")
    }

    @Test
    fun `should update freelance upon rc uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-rc.pdf"
        val freelance = FreelanceDomain.Builder(username)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(Optional.of(freelance))

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRcUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.rcFilename, "new-rc.pdf")
    }

    @Test
    fun `should create a new freelance upon rc uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-rc.pdf"

        val newFreelance = FreelanceDomain.Builder(username)
            .rcFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnRcUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertEquals(result.rcFilename, "new-rc.pdf")
    }

    @Test
    fun `should update freelance upon urssaf uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-urssaf.pdf"
        val freelance = FreelanceDomain.Builder(username)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(Optional.of(freelance))

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnUrssafUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.urssafFilename, "new-urssaf.pdf")
    }

    @Test
    fun `should create a new freelance upon urssaf uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-urssaf.pdf"

        val newFreelance = FreelanceDomain.Builder(username)
            .urssafFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnUrssafUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertEquals(result.urssafFilename, "new-urssaf.pdf")
    }

    @Test
    fun `should update freelance upon fiscal uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-fiscal.pdf"
        val freelance = FreelanceDomain.Builder(username)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(Optional.of(freelance))

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnFiscalUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.fiscalFilename, "new-fiscal.pdf")
    }

    @Test
    fun `should create a new freelance upon fiscal uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-fiscal.pdf"

        val newFreelance = FreelanceDomain.Builder(username)
            .fiscalFilename(filename)
            .build()
        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(any())).thenReturn(newFreelance)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.updateOnFiscalUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertEquals(result.fiscalFilename, "new-fiscal.pdf")
    }


    @Test
    fun `should send a notification by email upon company completion`() {
        // Arranges

        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain(username))
            .build()
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl",
            emailFrom = "emailFrom", smsFrom = "smsFrom",
            emailAdmin = "adminEmail", smsAdminNumber = "adminNumber")


        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.update(freelance)).thenReturn(Optional.of(freelance))
        Mockito.`when`(dataAdapter.sendEmail(eq("kong@gmail.com"), any(EmailNotificationDomain::class.java))).thenReturn(true)

        service = FreelanceService(dataAdapter)

        // Act
        val result = service.completed(freelance, metaNotification)

        // Arrange
        Mockito.verify(dataAdapter).sendEmail(eq("kong@gmail.com"), any(EmailNotificationDomain::class.java))
        Mockito.verify(dataAdapter).companyCompleted(username)
        Assertions.assertTrue(result.state?.completed!!)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}