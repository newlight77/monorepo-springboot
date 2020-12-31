package io.tricefal.core.security.ip

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class IpAddressEventHandler(
    private val ipAddressRepositoryAdapter: IpAddressRepositoryAdapter,
    private val ipAddressEventPublisher: IpAddressEventPublisher) {

    private var ipAddressesCache: Cache<String, IpAddressEvent> = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(20, TimeUnit.MINUTES)
        .refreshAfterWrite(2, TimeUnit.MINUTES)
        .build { this.find(ipAddress = it) }

    fun create(ipAddressEvent: IpAddressEvent) {
        ipAddressRepositoryAdapter.save(toEntity(ipAddressEvent))
    }

    fun find(ipAddress: String): IpAddressEvent? {
        return ipAddressRepositoryAdapter.findByIpAddress(ipAddress).map { fromEntity(it) }.firstOrNull()
    }

    fun filter(ipAddress: String): Boolean {
        val event = ipAddressesCache.get(ipAddress, this::find)
        return if(event?.isBlackListed() == true) true
                else {
                    val ipAddressEvent = IpAddressEvent(
                        lastDate = Instant.now(),
                        ipAddress = ipAddress,
                        status = Status.NONE
                    )
                    ipAddressEventPublisher.publishIpAddressEvent(ipAddressEvent)
                    false
                }
    }

    fun publishIpAddressEvent(ipAddressEvent: IpAddressEvent) {
        ipAddressEventPublisher.publishIpAddressEvent(ipAddressEvent)
    }

}