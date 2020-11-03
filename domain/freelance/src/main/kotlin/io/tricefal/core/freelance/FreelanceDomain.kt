package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain
import java.time.Instant

data class FreelanceDomain
    constructor(
            val username: String,
            val contact: ContactDomain?,
            val company: CompanyDomain?,
            val privacyDetail: PrivacyDetailDomain?,

            var kbisFile: MetafileDomain?,
            var ribFile: MetafileDomain?,
            var rcFile: MetafileDomain?,
            var urssafFile: MetafileDomain?,
            var fiscalFile: MetafileDomain?,

            var status: Status?,
            var state: FreelanceStateDomain?,
            var lastDate: Instant?
    ) {

    data class Builder(
            val username: String,
            var contact: ContactDomain? = null,
            var company: CompanyDomain? = null,
            var privacyDetail: PrivacyDetailDomain? = null,

            var kbisFile: MetafileDomain? = null,
            var ribFile: MetafileDomain? = null,
            var rcFile: MetafileDomain? = null,
            var urssafFile: MetafileDomain? = null,
            var fiscalFile: MetafileDomain? = null,

            var status: Status? = null,
            var state: FreelanceStateDomain? = null,
            var lastDate: Instant? = null

    ) {
        fun contact(contact: ContactDomain?) = apply { this.contact = contact ?: ContactDomain.Builder(username).build() }
        fun company(company: CompanyDomain?) = apply { this.company = company ?: CompanyDomain.Builder(username).build() }
        fun privacyDetail(privacyDetail: PrivacyDetailDomain?) = apply { this.privacyDetail = privacyDetail ?: PrivacyDetailDomain.Builder(username).build() }

        fun kbisFile(kbisFile: MetafileDomain?) = apply { this.kbisFile = kbisFile }
        fun ribFile(ribFile: MetafileDomain?) = apply { this.ribFile = ribFile }
        fun rcFile(rcFile: MetafileDomain?) = apply { this.rcFile = rcFile }
        fun urssafFile(urssafFile: MetafileDomain?) = apply { this.urssafFile = urssafFile }
        fun fiscalFile(fiscalFile: MetafileDomain?) = apply { this.fiscalFile = fiscalFile }

        fun status(status: Status?) = apply { this.status = status }
        fun state(state: FreelanceStateDomain?) = apply { this.state = state ?: FreelanceStateDomain.Builder(username).build() }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = FreelanceDomain(
                username,
                contact,
                company,
                privacyDetail,
                kbisFile,
                ribFile,
                rcFile,
                urssafFile,
                fiscalFile,
                status,
                state,
                lastDate
        )
    }
}

enum class Status {
    NONE,
    IN_MISSION,
    AVAILABLE_SOON,
    AVAILABLE,
    LOOKING;
}