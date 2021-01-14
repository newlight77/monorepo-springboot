package io.tricefal.core.command

import io.tricefal.core.freelance.AddressDomain
import io.tricefal.core.freelance.ContactDomain
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
class CommandServiceTest {

    @Mock
    lateinit var dataAdapter: CommandDataAdapter

    lateinit var service: ICommandService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(dataAdapter)
    }

    @Test
    fun `should do a create`() {
        // Arranges
        val command = CommandDomain.Builder("command name")
                .build()

        Mockito.`when`(dataAdapter.findByName("command name")).thenReturn(Optional.empty())
        Mockito.`when`(dataAdapter.create(command)).thenReturn(command)

        service = CommandService(dataAdapter)

        // Act
        val result = service.create(command)

        // Arrange
        Mockito.verify(dataAdapter).create(command)
        Assertions.assertEquals(command.companyName, result.companyName)
    }

    @Test
    fun `should find a command by company name`() {
        // Arrange
        val commandName = "command name"
        val command = CommandDomain.Builder(commandName)
                .build()

        Mockito.`when`(dataAdapter.findByName(commandName)).thenReturn(Optional.of(command))

        service = CommandService(dataAdapter)

        // Act
        val result = service.findByName(commandName)

        // Arrange
        Assertions.assertEquals(command.companyName, result.companyName)
    }


    @Test
    fun `should patch the command name with new company name`() {
        // Arrange
        val companyName = "command name"
        val date = Instant.now()

        val command = CommandDomain.Builder(companyName)
            .address(AddressDomain.Builder(companyName).build())
            .contact(ContactDomain.Builder().build())
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(command))
        Mockito.`when`(dataAdapter.update(any(CommandDomain::class.java))).thenReturn(command)

        val ops = listOf(PatchOperation.Builder("replace").path("/companyName").value("new name").build())

        service = CommandService(dataAdapter)

        // Act
        val result = service.patch(companyName, ops)

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.companyName).isEqualTo("new name")
    }

    @Test
    fun `should patch a command domain with a non existing child address`() {
        // Arrange
        val companyName = "command name"
        val date = Instant.now()

        val command = CommandDomain.Builder(companyName)
            .address(AddressDomain.Builder(companyName).build())
            .contact(ContactDomain.Builder().build())
            .lastDate(date)
            .build()

        Mockito.`when`(dataAdapter.findByName(companyName)).thenReturn(Optional.of(command))
        Mockito.`when`(dataAdapter.update(any(CommandDomain::class.java))).thenReturn(command)

        val ops = listOf(PatchOperation.Builder("replace").path("/address/city").value("paris").build())

        service = CommandService(dataAdapter)

        // Act
        val result = service.patch(companyName, ops)

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.address?.city).isEqualTo("paris")
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}