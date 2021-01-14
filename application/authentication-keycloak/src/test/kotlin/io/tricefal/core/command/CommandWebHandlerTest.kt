package io.tricefal.core.command

import io.tricefal.core.InfrastructureMockBeans
import io.tricefal.core.freelance.AddressModel
import io.tricefal.core.freelance.ContactModel
import io.tricefal.shared.util.json.PatchOperation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
@DataJpaTest
@ContextConfiguration(classes = [InfrastructureMockBeans::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandWebHandlerTest {
    @Autowired
    lateinit var webHandler: CommandWebHandler

    @Autowired
    lateinit var commandService: ICommandService

    @MockBean
    lateinit var jpaRepository: CommandJpaRepository

    @Test
    fun `should create a command successfully`() {
        // Arrange
        val username = "kong@gmail.com"
        val address = AddressModel.Builder().build()
        val contact = ContactModel.Builder(email = username).build()
        val command = CommandModel.Builder(username)
                .address(address)
                .contact(contact)
                .paymentConditionDays(30)
                .paymentMode("wired transfer")
                .build()
        val commandEntity = toEntity(fromModel(command))

        Mockito.`when`(jpaRepository.save(any(CommandEntity::class.java))).thenReturn(commandEntity)
        Mockito.`when`(jpaRepository.findByName("kong@gmail.com")).thenReturn(
//                listOf(),
                listOf(commandEntity))

        // Act
        val result = webHandler.create(command)

        // Arrange
        Assertions.assertEquals("wired transfer", result.paymentMode)
    }

    @Test
    fun `should find a command by name`() {
        // Arrange
        val username = "kong@gmail.com"
        val address = AddressModel.Builder().build()
        val contact = ContactModel.Builder(email = username).build()
        val command = CommandModel.Builder(username)
            .address(address)
            .contact(contact)
            .paymentConditionDays(30)
            .paymentMode("wired transfer")
            .build()
        val commandEntity = toEntity(fromModel(command))

        Mockito.`when`(jpaRepository.findByName(username)).thenReturn(listOf(commandEntity))

        // Act
        val result = webHandler.findByName(username)

        // Arrange
        Assertions.assertEquals(username, result.companyName)
    }


    @Test
    fun `should patch the command availability`() {
        // Arrange
        val username = "kong@gmail.com"
        val address = AddressModel.Builder().build()
        val contact = ContactModel.Builder(email = username).build()
        val command = CommandModel.Builder(username)
            .address(address)
            .contact(contact)
            .paymentConditionDays(30)
            .paymentMode("wired transfer")
            .build()
        val commandEntity = toEntity(fromModel(command))

        Mockito.`when`(jpaRepository.findByName(username)).thenReturn(listOf(commandEntity))
        Mockito.`when`(jpaRepository.save(any(CommandEntity::class.java))).thenReturn(commandEntity)

        val ops = listOf(
            PatchOperation.Builder("replace").path("/paymentMode").value("wired transfer").build(),
            PatchOperation.Builder("replace").path("/contact/lastName").value("new name").build()
        )

        // Act
        val result = webHandler.patch(username, ops)

        // Arrange
        Assertions.assertEquals("wired transfer", result.paymentMode)
        Assertions.assertEquals("new name", result.contact?.lastName)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

}