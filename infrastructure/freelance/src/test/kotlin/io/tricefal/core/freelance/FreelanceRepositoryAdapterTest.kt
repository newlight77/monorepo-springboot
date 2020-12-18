package io.tricefal.core.freelance

import io.tricefal.shared.util.json.PatchOperation
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FreelanceRepositoryAdapterTest {

    @Mock
    lateinit var jpaRepository: FreelanceJpaRepository

    lateinit var repositoryAdapter: FreelanceRepositoryAdapter

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(jpaRepository)
        repositoryAdapter = FreelanceRepositoryAdapter(jpaRepository)
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

        val domain = FreelanceDomain.Builder(username).status(Status.NONE).build()
        val entity = toEntity(domain)
        Mockito.`when`(jpaRepository.findByUsername(username)).thenReturn(
            listOf(entity)
        )

        val transientDomain = FreelanceDomain.Builder(username).status(Status.AVAILABLE).build()
        val transientEntity = toEntity(transientDomain)

        Mockito.`when`(jpaRepository.save(transientEntity)).thenReturn(transientEntity)

        val ops = listOf(PatchOperation.Builder("replace").path("/status").value(Status.AVAILABLE).build())

        // Act
        val result = repositoryAdapter.patch(freelance = domain, ops)

        // Assert
        Assertions.assertThat(result.isPresent).isTrue
        Assertions.assertThat(result.get().status).isEqualTo(Status.AVAILABLE)
    }
}