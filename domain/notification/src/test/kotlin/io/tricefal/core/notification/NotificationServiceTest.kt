package io.tricefal.core.notification

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NotifictionServiceTest {

    @Mock
    lateinit var adapter: INotificationAdapter

    lateinit var service: INotificationService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should send an email notification`() {
        // Arranges

        // Act

        // Arrange
    }

    @Test
    fun `should send a sms notification`() {
        // Arranges

        // Act

        // Arrange
    }

}