package io.oneprofile.signup.security.ip

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
            logger.error("Failed to publish an ip address event has been published $ipAddressEvent.ipAddress", ex)
            throw IpAddressPublicationException("Failed to publish an ip address event has been published $ipAddressEvent.ipAddress", ex)
        }
    }
}

class IpAddressPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
