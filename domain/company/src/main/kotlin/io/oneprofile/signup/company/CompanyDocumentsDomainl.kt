package io.oneprofile.signup.company

import java.time.Instant

data class CompanyDocumentsDomain(
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

        fun build() = CompanyDocumentsDomain(
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
