package io.oneprofile.core.profile

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

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "state")
        var state: ProfileStateEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now(),

        @Column(name = "portrait")
        var portrait: String? = null,

        @Column(name = "resume")
        var resume: String? = null,

        @Column(name = "resume_linkedin")
        var resumeLinkedin: String? = null
)

fun toEntity(domain: ProfileDomain): ProfileEntity {
        return ProfileEntity(
                null,
                username = domain.username,
                firstname = domain.firstname,
                lastname = domain.lastname,
                phoneNumber = domain.phoneNumber,
                status = domain.status.toString(),
                state = domain.state?.let { toEntity(it) },
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
                status = Status.valueOf(entity.status),
                state = entity.state?.let { fromEntity(it) },
                lastDate = entity.lastDate,
                portraitFilename = entity.portrait,
                resumeFilename = entity.resume,
                resumeLinkedinFilename = entity.resumeLinkedin
        )
}