package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "company_documents")
data class CompanyDocumentsEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Column(name = "kbis_filename", length = 255)
        val kbisFilename: String? = null,

        @Column(name = "rib_filename", length = 255)
        val ribFilename: String? = null,

        @Column(name = "rc_filename", length = 255)
        val rcFilename: String? = null,

        @Column(name = "urssaf_filename", length = 255)
        val urssafFilename: String? = null,

        @Column(name = "fiscal_filename", length = 255)
        val fiscalFilename: String? = null,

        @Column(name = "kbis_update_date")
        var kbisUpdateDate: Instant? = Instant.now(),

        @Column(name = "rib_update_date")
        var ribUpdateDate: Instant? = Instant.now(),

        @Column(name = "rc_update_date")
        var rcUpdateDate: Instant? = Instant.now(),

        @Column(name = "urssaf_update_date")
        var urssafUpdateDate: Instant? = Instant.now(),

        @Column(name = "fiscal_update_date")
        var fiscalUpdateDate: Instant? = Instant.now(),

)

fun toEntity(domain: CompanyDocumentsDomain): CompanyDocumentsEntity {
        return CompanyDocumentsEntity(
                id = null,

                kbisFilename = domain.kbisFilename,
                ribFilename = domain.ribFilename,
                rcFilename = domain.rcFilename,
                urssafFilename = domain.urssafFilename,
                fiscalFilename = domain.fiscalFilename,

                kbisUpdateDate = domain.kbisUpdateDate,
                ribUpdateDate = domain.ribUpdateDate,
                rcUpdateDate = domain.rcUpdateDate,
                urssafUpdateDate = domain.urssafUpdateDate,
                fiscalUpdateDate = domain.fiscalUpdateDate,
        )
}

fun fromEntity(entity: CompanyDocumentsEntity): CompanyDocumentsDomain {
        return CompanyDocumentsDomain(
                kbisFilename = entity.kbisFilename,
                ribFilename = entity.ribFilename,
                rcFilename = entity.rcFilename,
                urssafFilename = entity.urssafFilename,
                fiscalFilename = entity.fiscalFilename,

                kbisUpdateDate = entity.kbisUpdateDate,
                ribUpdateDate = entity.ribUpdateDate,
                rcUpdateDate = entity.rcUpdateDate,
                urssafUpdateDate = entity.urssafUpdateDate,
                fiscalUpdateDate = entity.fiscalUpdateDate,
        )
}