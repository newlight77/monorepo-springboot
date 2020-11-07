package io.tricefal.core.profile

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
        val id: Long?,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "last_date")
        var lastDate: Instant? = null,

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
                lastDate = domain.lastDate,
                portrait = domain.portraitFilename,
                resume = domain.resumeFilename,
                resumeLinkedin = domain.resumeLinkedinFilename
        )
}

fun fromEntity(entity: ProfileEntity): ProfileDomain {
        return ProfileDomain(
                username = entity.username,
                lastDate = entity.lastDate,
                portraitFilename = entity.portrait,
                resumeFilename = entity.resume,
                resumeLinkedinFilename = entity.resumeLinkedin
        )
}