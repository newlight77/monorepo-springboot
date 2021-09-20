package io.oneprofile.signup.security.ip

import java.time.Instant

data class IpAddressEvent(
    val lastDate: Instant = Instant.now(),
    val ipAddress: String,
    val status: Status) {

    fun isBlackListed(): Boolean {
        return status == Status.BLACKLIST
    }
}

fun toEntity(event: IpAddressEvent): IpAddressEntity {
    return IpAddressEntity (
        id = null,
        lastDate = event.lastDate,
        ipAddress = event.ipAddress,
        status = event.status.toString()
    )
}

fun fromEntity(entity: IpAddressEntity): IpAddressEvent {
    return IpAddressEvent (
        lastDate = entity.lastDate,
        ipAddress = entity.ipAddress,
        status = entity.status.toUpperCase().let { Status.valueOf(it) }
    )
}