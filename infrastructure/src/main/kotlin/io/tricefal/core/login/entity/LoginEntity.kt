package io.tricefal.core.login.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.tricefal.core.account.entity.AuthorityEntity
import io.tricefal.core.login.domain.EMAIL_REGEX
import io.tricefal.core.login.domain.LoginDomain
import org.hibernate.annotations.BatchSize
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "login_detail")
data class LoginEntity(
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        @Id val id: Long,

        @NotNull
        @Pattern(regexp = EMAIL_REGEX)
        @Size(min = 10, max = 50)
        @Column(name = "expiration_date", length = 50, unique = true, nullable = false)
        var username: String,

        @Column(name = "last_login")
        var lastLogin: Instant,

        @Column(name = "success")
        var success: Boolean
) {

        @JsonIgnore
        @ManyToMany
        @JoinTable(name = "jhi_user_authority",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "name")])
        @BatchSize(size = 20)
        var authorities: Set<AuthorityEntity> = HashSet<AuthorityEntity>()
}

fun fromDomain(domain: LoginDomain): LoginEntity {
        var entity = LoginEntity(
                domain.id,
                domain.username,
                domain.lastLogin,
                domain.success
        )
        entity.authorities = domain.authorities.map { AuthorityEntity(it) }.toSet()
        return entity
}

fun toDomain(entity: LoginEntity): LoginDomain {
        entity.authorities
        return LoginDomain(
                entity.id,
                entity.username,
                entity.lastLogin,
                entity.success,
                entity.authorities.map { it.name }.toSet()
        );
}