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
        val id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "firstname", length = 50)
        var firstname: String? = null,

        @Column(name = "lastname", length = 50)
        var lastname: String? = null,

        @Column(name = "phone", length = 50)
        var phoneNumber: String? = null,

        @Column(name = "activation_code")
        var activationCode: String? = null,

        @Column(name = "status", length = 50)
        var status: String,

        @Column(name = "signup_date")
        var signupDate: Instant? = null,

        @OneToOne
        @JoinColumn(name = "state")
        var signupState: SignupStateEntity? = null
)

fun toEntity(domain: SignupDomain): SignupEntity {
        return SignupEntity(
                null,
                domain.username,
                domain.firstname,
                domain.lastname,
                domain.phoneNumber,
                domain.activationCode,
                domain.status.toString(),
                domain.signupDate,
                toEntity(domain.state!!))
}

fun fromEntity(entity: SignupEntity): SignupDomain {
        return SignupDomain.Builder(entity.username)
                .firstname(entity.firstname)
                .lastname(entity.lastname)
                .phoneNumber(entity.phoneNumber)
                .activationCode(entity.activationCode)
                .status(Status.valueOf(entity.status))
                .signupDate(entity.signupDate)
                .state(fromEntity(entity.signupState!!))
                .build()
}