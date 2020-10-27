package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class MissionWishEventListener(val missionWishService: IMissionWishService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isFreelance()")
    fun handleMissionWishUpdatedEvent(event: MissionWishUpdatedEvent) {
        try {
            val domain = MissionWishDomain.Builder(event.username).build()
            missionWishService.create(domain)
        } catch (ex: Exception) {
            logger.error("Failed to create a missionWish with username ${event.username}")
            throw MissionWishCreationException("Failed to create a missionWish with username ${event.username}")
        }
        logger.info("MissionWishEventListener picked up a MissionWishUpdatedEvent for ${event.username}")
    }

    class MissionWishCreationException(private val msg: String) : Throwable(msg) {}
}