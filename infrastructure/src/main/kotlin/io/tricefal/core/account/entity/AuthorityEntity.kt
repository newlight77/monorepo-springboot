package io.tricefal.core.account.entity

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "jhi_authority")
data class AuthorityEntity(@GeneratedValue
                           @Id @Column(length = 50)
                           var name: @NotNull @Size(max = 50) String
)
