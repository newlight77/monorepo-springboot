package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

@Entity
@Table(name = "freelance")
data class FreelanceEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String

)

fun toEntity(domain: FreelanceDomain): FreelanceEntity {
        return FreelanceEntity(
                null,
                domain.username)
}

fun fromEntity(entity: FreelanceEntity): FreelanceDomain {
        return FreelanceDomain.Builder(entity.username)
                .build()
}