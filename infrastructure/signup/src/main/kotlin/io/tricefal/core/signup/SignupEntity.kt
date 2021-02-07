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
        var id: Long? = null,

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

        @Column(name = "activation_token")
        var activationToken: String? = null,

        @Column(name = "status", length = 50)
        var status: String,

        @Column(name = "cgu_version", length = 50)
        var cguAcceptedVersion: String? = null,

        @Column(name = "signup_date")
        var signupDate: Instant? = null,

        @Column(name = "resume_filename")
        var resumeFilename: String? = null,

        @Column(name = "resume_linkedin")
        var resumeLinkedinFilename: String? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "state")
        var signupState: SignupStateEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "comment")
        var comment: CommentEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = null,

)

fun toEntity(domain: SignupDomain): SignupEntity {
        return SignupEntity(
                id = null,
                username = domain.username,
                firstname = domain.firstname,
                lastname = domain.lastname,
                phoneNumber = domain.phoneNumber,
                activationCode = domain.activationCode,
                activationToken = domain.activationToken,
                status = domain.status.toString(),
                cguAcceptedVersion = domain.cguAcceptedVersion,
                signupDate = domain.signupDate,
                resumeFilename = domain.resumeFilename,
                resumeLinkedinFilename = domain.resumeLinkedinFilename,
                signupState = domain.state?.let { toEntity(it) },
                comment = domain.comment?.let { toEntity(it) },
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: SignupEntity): SignupDomain {
        return SignupDomain.Builder(entity.username)
                .firstname(entity.firstname)
                .lastname(entity.lastname)
                .phoneNumber(entity.phoneNumber)
                .activationCode(entity.activationCode)
                .activationToken(entity.activationToken)
                .status(toStatus(entity.status))
                .cguAcceptedVersion(entity.cguAcceptedVersion)
                .signupDate(entity.signupDate)
                .resumeFilename(entity.resumeFilename)
                .resumeLinkedinFilename(entity.resumeLinkedinFilename)
                .state(entity.signupState?.let { fromEntity(it) })
                .lastDate(entity.lastDate)
                .comment(entity.comment?.let { fromEntity(it) })
                .build()
}