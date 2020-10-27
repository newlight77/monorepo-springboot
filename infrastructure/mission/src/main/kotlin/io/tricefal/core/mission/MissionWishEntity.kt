package io.tricefal.core.mission

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
        var location: String? = null

)

fun toEntity(domain: MissionWishDomain): MissionWishEntity {
        return MissionWishEntity(
                null,
                domain.username,
                domain.summary,
                domain.technologies,
                domain.domains,
                domain.clients,
                domain.dailyFee,
                domain.location
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
                .build()
}
