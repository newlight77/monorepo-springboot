package io.tricefal.core.user

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    lateinit var dataAdapter: UserDataAdapter

    private lateinit var service: UserService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(dataAdapter)
        service = UserService(dataAdapter)
    }

    @Test
    fun `should update the password of a user`() {
        // Arrange
        val username = "kong@gmail.com"
        val newPassword = "newpassword"

        Mockito.`when`(dataAdapter.updatePassword(username, newPassword)).thenReturn(true)

        // Act
        val result = service.updatePassword(username, newPassword)

        // Arrange
        Assertions.assertTrue(result)
        Mockito.verify(dataAdapter).updatePassword("kong@gmail.com", "newpassword")
    }

}