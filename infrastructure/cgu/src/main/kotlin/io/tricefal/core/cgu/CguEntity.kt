package io.tricefal.core.cgu

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

@Entity
@Table(name = "cgu")
data class CguEntity (
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "cgu_version", length = 50)
        var cguVersion: String? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: CguDomain): CguEntity {
        return CguEntity(
                null,
                domain.username,
                domain.acceptedCguVersion,
                domain.lastDate)
}

fun fromEntity(entity: CguEntity): CguDomain {
        return CguDomain.Builder(entity.username)
                .acceptedCguVersion(entity.cguVersion)
                .lastDate(entity.lastDate)
                .build()
}
