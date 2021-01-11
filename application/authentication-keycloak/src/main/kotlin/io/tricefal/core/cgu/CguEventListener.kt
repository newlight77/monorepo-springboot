package io.tricefal.core.cgu

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.lang.Exception
import java.time.Instant

@Component
class CguEventListener(val cguService: ICguService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handleCguAcceptedEvent(event: CguAcceptedEvent) {
        try {
            cguService.save(event.username, event.cguAcceptedVersion)
        } catch (ex: Throwable) {
            throw CguAcceptedSavingException("Failed to save a cgu accepted event for user ${event.username} with version ${event.cguAcceptedVersion}")
        }
        logger.info("CguEventListener picked up a CguStatusUpdatedEvent for user ${event.username} with version \${event.cguAcceptedVersion")
    }

    class CguAcceptedSavingException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }

}