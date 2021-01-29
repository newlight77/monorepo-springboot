package io.tricefal.core.mission

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

@Entity
@Table(name = "mission")
data class MissionWishEntity (
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "username", length = 50)
        var username: String,

        @Column(name = "summary", length = 200)
        var summary: String? = null,

        @Column(name = "technologies", length = 500)
        var technologies: String? = null,

        @Column(name = "domains", length = 200)
        var domains: String? = null,

        @Column(name = "clients", length = 300)
        var clients: String? = null,

        @Column(name = "daily_fee", length = 300)
        var dailyFee: String? = null,

        @Column(name = "location", length = 50)
        var location: String? = null,

        @Column(name = "mission_abroad", length = 150)
        var missionAbroad: String? = null,

        @Column(name = "cv_filename", length = 300)
        var resumeFilename: String? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: MissionWishDomain): MissionWishEntity {
        return MissionWishEntity(
                id = null,
                username = domain.username,
                summary = domain.summary,
                technologies = domain.technologies,
                domains = domain.domains,
                clients = domain.clients,
                dailyFee = domain.dailyFee,
                location = domain.location,
                missionAbroad = domain.missionAbroad,
                resumeFilename = domain.resumeFilename,
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: MissionWishEntity): MissionWishDomain {
        return MissionWishDomain.Builder(entity.username)
                .summary(entity.summary)
                .technologies(entity.technologies)
                .domains(entity.domains)
                .clients(entity.clients)
                .dailyFee(entity.dailyFee)
                .location(entity.location)
                .missionAbroad(entity.missionAbroad)
                .resumeFilename(entity.resumeFilename)
                .lastDate(entity.lastDate)
                .build()
}
