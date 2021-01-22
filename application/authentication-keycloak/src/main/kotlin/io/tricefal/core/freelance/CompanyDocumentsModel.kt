package io.tricefal.core.freelance

import java.time.Instant

data class CompanyDocumentsModel(
    var kbisFilename:  String?,
    var ribFilename:  String?,
    var rcFilename:  String?,
    var urssafFilename:  String?,
    var fiscalFilename:  String?,

    var kbisUpdateDate: Instant?,
    var ribUpdateDate: Instant?,
    var rcUpdateDate: Instant?,
    var urssafUpdateDate: Instant?,
    var fiscalUpdateDate: Instant?
) {
    data class Builder(
        var kbisFilename:  String? = null,
        var ribFilename:  String? = null,
        var rcFilename:  String? = null,
        var urssafFilename:  String? = null,
        var fiscalFilename:  String? = null,

        var kbisUpdateDate: Instant? = null,
        var ribUpdateDate: Instant? = null,
        var rcUpdateDate: Instant? = null,
        var urssafUpdateDate: Instant? = null,
        var fiscalUpdateDate: Instant? = null
    ) {
        fun kbisFilename(kbisFilename: String?) = apply { this.kbisFilename = kbisFilename }
        fun ribFilename(ribFilename: String?) = apply { this.ribFilename = ribFilename }
        fun rcFilename(rcFilename: String?) = apply { this.rcFilename = rcFilename }
        fun urssafFilename(urssafFilename: String?) = apply { this.urssafFilename = urssafFilename }
        fun fiscalFilename(fiscalFilename: String?) = apply { this.fiscalFilename = fiscalFilename }

        fun kbisUpdateDate(kbisUpdateDate: Instant?) = apply { this.kbisUpdateDate = kbisUpdateDate }
        fun ribUpdateDate(ribUpdateDate: Instant?) = apply { this.ribUpdateDate = ribUpdateDate }
        fun rcUpdateDate(rcUpdateDate: Instant?) = apply { this.rcUpdateDate = rcUpdateDate }
        fun urssafUpdateDate(urssafUpdateDate: Instant?) = apply { this.urssafUpdateDate = urssafUpdateDate }
        fun fiscalUpdateDate(fiscalUpdateDate: Instant?) = apply { this.fiscalUpdateDate = fiscalUpdateDate }

        fun build() = CompanyDocumentsModel(
            kbisFilename = kbisFilename,
            ribFilename = ribFilename,
            rcFilename = rcFilename,
            urssafFilename = urssafFilename,
            fiscalFilename = fiscalFilename,
            kbisUpdateDate = kbisUpdateDate,
            ribUpdateDate = ribUpdateDate,
            rcUpdateDate = rcUpdateDate,
            urssafUpdateDate = urssafUpdateDate,
            fiscalUpdateDate = fiscalUpdateDate
        )
    }
}

fun toModel(domain: CompanyDocumentsDomain): CompanyDocumentsModel {
    return CompanyDocumentsModel(
        kbisFilename = domain.kbisFilename,
        ribFilename = domain.ribFilename,
        rcFilename = domain.rcFilename,
        urssafFilename = domain.urssafFilename,
        fiscalFilename = domain.fiscalFilename,
        kbisUpdateDate = domain.kbisUpdateDate,
        ribUpdateDate = domain.ribUpdateDate,
        rcUpdateDate = domain.rcUpdateDate,
        urssafUpdateDate = domain.urssafUpdateDate,
        fiscalUpdateDate = domain.fiscalUpdateDate
    )
}

fun fromModel(model: CompanyDocumentsModel) : CompanyDocumentsDomain {
    return CompanyDocumentsDomain(
        kbisFilename = model.kbisFilename,
        ribFilename = model.ribFilename,
        rcFilename = model.rcFilename,
        urssafFilename = model.urssafFilename,
        fiscalFilename = model.fiscalFilename,
        kbisUpdateDate = model.kbisUpdateDate,
        ribUpdateDate = model.ribUpdateDate,
        rcUpdateDate = model.rcUpdateDate,
        urssafUpdateDate = model.urssafUpdateDate,
        fiscalUpdateDate = model.fiscalUpdateDate
    )
}