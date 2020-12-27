package io.tricefal.core.notification

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotificationConfiguration {

    @Bean
    fun notificationService(notiifiicationAdapter: NotificationAdapter): INotificationService {
        return NotificationService(notiifiicationAdapter)
    }

}