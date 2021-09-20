package io.oneprofile.signup.security.ip

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class IpAddressEventListener(val ipAddressEventHandler: IpAddressEventHandler) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handleIpAddressEvent(event: IpAddressEvent) {
        try {
            ipAddressEventHandler.create(event)
        } catch (ex: Throwable) {
            throw IpAddressEventHandlerException("Failed to create an ip address event with username ${event.ipAddress}")
        }
        logger.info("LoginEventListener picked up an ip address event with ${event.ipAddress}")
    }
}

class IpAddressEventHandlerException(private val msg: String) : Throwable(msg) {}
