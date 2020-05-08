package io.tricefal.core.signup

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

@Entity
@Table(name = "signup")
data class SignupEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        val id: Long,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "firstname", length = 50)
        var firstname: String,

        @Column(name = "lastname", length = 50)
        var lastname: String,

        @Column(name = "phone", length = 50)
        var phoneNumber: String,

        @Column(name = "signup_date")
        var signupDate: Instant,

        @Column(name = "activation_code")
        var activationCode: String,

        @Column(name = "status", length = 50)
        var status: String
)

fun toEntity(domain: SignupDomain): SignupEntity {
        return SignupEntity(
                domain.id,
                domain.username,
                domain.firstname,
                domain.lastname,
                domain.phoneNumber,
                domain.signupDate,

                domain.activationcode,
                domain.status.toString()
        )
}

fun fromEntity(entity: SignupEntity): SignupDomain {
        return SignupDomain(
                entity.id,
                entity.username,
                entity.firstname,
                entity.lastname,
                entity.phoneNumber,
                entity.signupDate,

                entity.activationCode,
                Status.valueOf(entity.status)
        )
}