package io.tricefal.core.command

import io.tricefal.core.freelance.AddressEntity
import io.tricefal.core.freelance.ContactEntity
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "company")
data class CommandEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Size(min = 3, max = 100)
        @Column(name = "company_name", length = 100)
        val companyName: String,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "fiscal_address_id")
        var address: AddressEntity? = null,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "admin_contact_id")
        var contact: ContactEntity? = null,

        @Column(name = "payment_condition", length = 100)
        val paymentConditionDays: Int? = null,

        @Column(name = "payment_mode", length = 100)
        val paymentMode: String? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: CommandDomain): CommandEntity {
        return CommandEntity(
                id = null,
                companyName = domain.companyName,
                address = domain.address?.let { io.tricefal.core.freelance.toEntity(it) },
                contact = domain.contact?.let { io.tricefal.core.freelance.toEntity(it) },
                paymentConditionDays = domain.paymentConditionDays,
                paymentMode = domain.paymentMode,
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: CommandEntity): CommandDomain {
        return CommandDomain(
                companyName = entity.companyName,
                address = entity.address?.let { io.tricefal.core.freelance.fromEntity(it) },
                contact = entity.contact?.let { io.tricefal.core.freelance.fromEntity(it) },
                paymentConditionDays = entity.paymentConditionDays,
                paymentMode = entity.paymentMode,
                lastDate = entity.lastDate
        )
}