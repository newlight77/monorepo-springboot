package io.tricefal.core.freelance

import java.time.Instant

data class FreelanceDomain
    constructor(
            val username: String,
            val contact: ContactDomain?,
            val company: CompanyDomain?,
            val privacyDetail: PrivacyDetailDomain?,

            var kbisFilename: String?,
            var ribFilename: String?,
            var rcFilename: String?,
            var urssafFilename: String?,
            var fiscalFilename: String?,

            var status: Status?,
            var state: FreelanceStateDomain?,
            var lastDate: Instant?
    ) {

    data class Builder(
            val username: String,
            var contact: ContactDomain? = null,
            var company: CompanyDomain? = null,
            var privacyDetail: PrivacyDetailDomain? = null,

            var kbisFilename: String? = null,
            var ribFilename: String? = null,
            var rcFilename: String? = null,
            var urssafFilename: String? = null,
            var fiscalFilename: String? = null,

            var status: Status? = null,
            var state: FreelanceStateDomain? = null,
            var lastDate: Instant? = null

    ) {
        fun contact(contact: ContactDomain?) = apply { this.contact = contact ?: ContactDomain.Builder(username).build() }
        fun company(company: CompanyDomain?) = apply { this.company = company ?: CompanyDomain.Builder(username).build() }
        fun privacyDetail(privacyDetail: PrivacyDetailDomain?) = apply { this.privacyDetail = privacyDetail ?: PrivacyDetailDomain.Builder(username).build() }

        fun kbisFilename(kbisFilename: String?) = apply { this.kbisFilename = kbisFilename }
        fun ribFilename(ribFilename: String?) = apply { this.ribFilename = ribFilename }
        fun rcFilename(rcFilename: String?) = apply { this.rcFilename = rcFilename }
        fun urssafFilename(urssafFilename: String?) = apply { this.urssafFilename = urssafFilename }
        fun fiscalFilename(fiscalFilename: String?) = apply { this.fiscalFilename = fiscalFilename }

        fun status(status: Status?) = apply { this.status = status }
        fun state(state: FreelanceStateDomain?) = apply { this.state = state ?: FreelanceStateDomain.Builder(username).build() }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = FreelanceDomain(
                username,
                contact,
                company,
                privacyDetail,
                kbisFilename,
                ribFilename,
                rcFilename,
                urssafFilename,
                fiscalFilename,
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