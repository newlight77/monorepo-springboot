package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class MissionWishEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishStatusUpdatedEvent(username: String, status: String) {
        try {
            applicationEventPublisher.publishEvent(
                    MissionWishUpdatedEvent(username)
            )
            logger.info("A MissionWishUpdatedEvent has been published user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a MissionWishUpdatedEvent for user $username")
            throw MissionWishUpdatedPublicationException("Failed to publish a MissionWishUpdatedEvent for user $username")
        }
    }

}

class MissionWishUpdatedPublicationException(private val msg: String) : Throwable(msg) {}

