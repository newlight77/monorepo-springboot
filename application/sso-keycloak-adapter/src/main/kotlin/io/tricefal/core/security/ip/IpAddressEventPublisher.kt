package io.tricefal.core.security.ip

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class IpAddressEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishIpAddressEvent(ipAddressEvent: IpAddressEvent) {
        try {
            applicationEventPublisher.publishEvent(ipAddressEvent)
            logger.info("An ip address event has been published $ipAddressEvent.ipAddress")
        } catch (ex: Exception) {
            logger.error("Failed to publish an ip address event has been published $ipAddressEvent.ipAddress")
            throw IpAddressPublicationException("Failed to publish an ip address event has been published $ipAddressEvent.ipAddress")
        }
    }
}

class IpAddressPublicationException(private val msg: String) : Throwable(msg) {}
