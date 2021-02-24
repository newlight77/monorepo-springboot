package io.tricefal.core.cgu

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*


@ExtendWith(MockitoExtension::class)
class CguServiceTest {

    @Mock
    lateinit var repository: ICguAdapter

    lateinit var service: ICguService

    @Test
    fun `should create a login successfully`() {
        // Arrange
        val username = "kong@gmail.com"
        val cguVersion = "v1"
        val cgu = CguDomain(username, Instant.now(), cguVersion)
        Mockito.`when`(repository.save(cgu)).thenReturn(cgu)
        service = CguService(repository)
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(cgu))

        // Act
        val result = service.save(username, cguVersion)

        // Arrange
        Mockito.verify(repository).save(cgu)
        Assertions.assertEquals(cgu, result)
    }

    @Test
    fun `should retrieve last logins by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val cgu = CguDomain("kong@gmail.com", Instant.now(), "v1")

        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(cgu))
        service = CguService(repository)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(cgu, result.get())
    }
}