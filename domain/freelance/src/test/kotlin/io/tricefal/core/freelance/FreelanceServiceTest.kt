package io.tricefal.core.freelance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
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

}