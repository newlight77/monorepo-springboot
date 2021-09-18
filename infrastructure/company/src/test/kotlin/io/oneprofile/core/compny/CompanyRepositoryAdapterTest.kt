package io.oneprofile.core.compny

import io.oneprofile.core.company.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class CompanyRepositoryAdapterTest {

    @Mock
    lateinit var jpaRepository: CompanyJpaRepository

    @Mock
    lateinit var eventPublisher: CompanyEventPublisher

    lateinit var repositoryAdapter: CompanyRepositoryAdapter

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(jpaRepository)
        repositoryAdapter = CompanyRepositoryAdapter(jpaRepository, eventPublisher)
    }

    @Test
    fun `should create a company domain in persistence unit`() {
        // Arrange
        val companyName = "my company"
        val date = Instant.now()

        val domain = CompanyDomain.Builder(companyName)
            .lastDate(date)
            .build()
        val transientEntity = toEntity(domain)

        Mockito.`when`(jpaRepository.findByName(companyName)).thenReturn(listOf())
        Mockito.`when`(jpaRepository.save(any(CompanyEntity::class.java))).thenReturn(transientEntity)

        // Act
        val result = repositoryAdapter.create(domain)

        // Assert
        Assertions.assertThat(result.raisonSocial).isEqualTo(companyName)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}