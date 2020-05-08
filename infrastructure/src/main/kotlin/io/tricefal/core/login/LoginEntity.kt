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
        val id: Long,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "last_login")
        var lastLogin: Instant,

        @Column(name = "ip_address", length = 30)
        var ipAddress: String,

        @Column(name = "success")
        var success: Boolean
)

fun toEntity(domain: LoginDomain): LoginEntity {
        return LoginEntity(
                domain.id,
                domain.username,
                domain.lastLogin,
                domain.ipAddress,
                domain.success
        )
}

fun fromEntity(entity: LoginEntity): LoginDomain {
        return LoginDomain(
                entity.id,
                entity.username,
                entity.lastLogin,
                entity.ipAddress,
                entity.success
        )
}