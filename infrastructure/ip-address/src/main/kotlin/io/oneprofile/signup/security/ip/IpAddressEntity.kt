package io.oneprofile.signup.security.ip

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "ip_address")
data class IpAddressEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        val id: Long?,

        @Column(name = "last_date")
        var lastDate: Instant = Instant.now(),

        @Column(name = "ip", length = 30)
        var ipAddress: String,

        @Column(name = "status", length = 30)
        var status: String
)

enum class Status {
        NONE,
        BLACKLIST,
        WHITELIST
}