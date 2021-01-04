package io.tricefal.core.profile

import io.tricefal.core.signup.toStatus
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

@Entity
@Table(name = "profile")
data class ProfileEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long?,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "firstname", length = 50)
        var firstname: String? = null,

        @Column(name = "lastname", length = 50)
        var lastname: String? = null,

        @Column(name = "phoneNumber", length = 50)
        var phoneNumber: String? = null,

        @Column(name = "status", length = 50)
        var status: String,

        @Column(name = "signup_state", length = 50)
        var signupState: String,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now(),

        @Column(name = "portrait")
        var portrait: String? = null,

        @Column(name = "resume")
        var resume: String? = null,

        @Column(name = "resume_linkedin")
        var resumeLinkedin: String? = null
) {
}

fun toEntity(domain: ProfileDomain): ProfileEntity {
        return ProfileEntity(
                null,
                username = domain.username,
                firstname = domain.firstname,
                lastname = domain.lastname,
                phoneNumber = domain.phoneNumber,
                status = domain.status.toString(),
                signupState = domain.signupState.toString(),
                lastDate = domain.lastDate,
                portrait = domain.portraitFilename,
                resume = domain.resumeFilename,
                resumeLinkedin = domain.resumeLinkedinFilename
        )
}

fun fromEntity(entity: ProfileEntity): ProfileDomain {
        return ProfileDomain(
                username = entity.username,
                firstname = entity.firstname,
                lastname = entity.lastname,
                phoneNumber = entity.phoneNumber,
                status = toStatus(entity.status),
                signupState = toState(entity.signupState),
                lastDate = entity.lastDate,
                portraitFilename = entity.portrait,
                resumeFilename = entity.resume,
                resumeLinkedinFilename = entity.resumeLinkedin
        )
}