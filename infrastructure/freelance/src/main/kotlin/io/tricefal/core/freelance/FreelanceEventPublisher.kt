package io.tricefal.core.freelance

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class FreelanceEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishStatusUpdatedEvent(username: String, status: String) {
        applicationEventPublisher.publishEvent(
                FreelanceStatusUpdatedEvent(username, status)
        )
        logger.info("A FreelanceStatusUpdatedEvent has been published user $username")
    }

}

