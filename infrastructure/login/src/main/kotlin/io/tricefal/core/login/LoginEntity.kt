package io.tricefal.core.login

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "login")
data class LoginEntity(
//        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//        @SequenceGenerator(name = "sequenceGenerator")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        val id: Long?,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "login_date")
        var loginDate: Instant,

        @Column(name = "ip_address", length = 30)
        var ipAddress: String,

        @Column(name = "city", length = 50)
        var city: String?,

        @Column(name = "region", length = 100)
        var region: String?,

        @Column(name = "device", length = 100)
        val device: String?,

        @Column(name = "success")
        var success: Boolean?
)

fun toEntity(domain: LoginDomain): LoginEntity {
        return LoginEntity(
                id = null,
                username = domain.username,
                loginDate = domain.loginDate!!,
                ipAddress = domain.ipAddress!!,
                city = domain.city,
                region = domain.region,
                device = domain.device,
                success = domain.success
        )
}

fun fromEntity(entity: LoginEntity): LoginDomain {
        return LoginDomain(
                username = entity.username,
                loginDate = entity.loginDate,
                ipAddress = entity.ipAddress,
                city = entity.city,
                region = entity.region,
                device = entity.device,
                success = entity.success
        )
}