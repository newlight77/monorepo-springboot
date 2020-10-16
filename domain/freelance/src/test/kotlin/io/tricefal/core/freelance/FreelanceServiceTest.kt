package io.tricefal.core.freelance

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
    lateinit var adapter: IFreelanceAdapter

    lateinit var service: IFreelanceService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should do a freelance`() {
        // Arranges
        val freelance = FreelanceDomain.Builder("kong@gmail.com")
                .build()

        Mockito.`when`(adapter.create(freelance)).thenReturn(freelance)

        service = FreelanceService(adapter)

        // Act
        val result = service.create(freelance)

        // Arrange
        Mockito.verify(adapter).create(freelance)
        Assertions.assertEquals(freelance.username, result.username)
    }

    @Test
    fun `should find a freelance by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val freelance = FreelanceDomain.Builder(username)
                .build()

        Mockito.`when`(adapter.findByUsername(username)).thenReturn(Optional.of(freelance))

        service = FreelanceService(adapter)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(freelance.username, result.get().username)
    }

}