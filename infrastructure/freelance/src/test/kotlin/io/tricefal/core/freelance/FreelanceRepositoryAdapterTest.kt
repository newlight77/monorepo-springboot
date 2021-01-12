package io.tricefal.core.freelance

import io.tricefal.core.notification.INotificationAdapter
import io.tricefal.shared.util.json.PatchOperation
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class FreelanceRepositoryAdapterTest {

    @Mock
    lateinit var jpaRepository: FreelanceJpaRepository

    @Mock
    lateinit var eventPublisher: FreelanceEventPublisher

    @Mock
    lateinit var notificationAdapter: INotificationAdapter

    lateinit var repositoryAdapter: FreelanceRepositoryAdapter

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(jpaRepository)
        repositoryAdapter = FreelanceRepositoryAdapter(jpaRepository, notificationAdapter, eventPublisher)
    }

    @Test
    fun `should save a freelance domain in persistence unit`() {
        // Arrange

        // Act

        // Asseert
    }

    @Test
    fun `should patch a freelance domain in persistence unit`() {
        // Arrange
        val username = "kong@gmail.com"
        val date = Instant.now()

        val domain = FreelanceDomain.Builder(username)
            .availability(Availability.NONE)
            .lastDate(date)
            .build()
        val entity = toEntity(domain)
        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(
            listOf(entity)
        )

        val transientDomain = FreelanceDomain.Builder(username)
            .company(CompanyDomain.Builder().build())
            .contact(ContactDomain.Builder().build())
            .privacyDetail(PrivacyDetailDomain.Builder(username).build())
            .availability(Availability.AVAILABLE)
            .lastDate(date)
            .build()
        val transientEntity = toEntity(transientDomain)

        Mockito.`when`(jpaRepository.save(any(FreelanceEntity::class.java))).thenReturn(transientEntity)

        val ops = listOf(PatchOperation.Builder("replace").path("/status").value(Availability.AVAILABLE).build())

        // Act
        val result = repositoryAdapter.patch(freelance = domain, ops)

        // Assert
        Assertions.assertThat(result.isPresent).isTrue
        Assertions.assertThat(result.get().availability).isEqualTo(Availability.AVAILABLE)
    }

    @Test
    fun `should patch a freelance domain in persistence unit with a non existing child company`() {
        // Arrange
        val username = "kong@gmail.com"
        val date = Instant.now()

        val domain = FreelanceDomain.Builder(username)
            .availability(Availability.NONE)
            .lastDate(date)
            .build()
        val entity = toEntity(domain)
        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(
            listOf(entity)
        )

        val transientDomain = FreelanceDomain.Builder(username)
            .company(CompanyDomain.Builder().build())
            .contact(ContactDomain.Builder().build())
            .privacyDetail(PrivacyDetailDomain.Builder(username).build())
            .availability(Availability.AVAILABLE)
            .lastDate(date)
            .build()
        val transientEntity = toEntity(transientDomain)

        Mockito.`when`(jpaRepository.save(any(FreelanceEntity::class.java))).thenReturn(transientEntity)

        val ops = listOf(PatchOperation.Builder("replace").path("/company/raisonSocial").value("new raisonSocial").build())

        // Act
        val result = repositoryAdapter.patch(freelance = domain, ops)

        // Assert
        Assertions.assertThat(result.isPresent).isTrue
        Assertions.assertThat(result.get().availability).isEqualTo(Availability.AVAILABLE)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}