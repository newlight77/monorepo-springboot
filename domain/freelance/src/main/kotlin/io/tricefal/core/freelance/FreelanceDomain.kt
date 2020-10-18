package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain

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

            var state: FreelanceStateDomain?
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

            var state: FreelanceStateDomain? = null

    ) {
        fun contact(contact: ContactDomain?) = apply { this.contact = contact }
        fun company(company: CompanyDomain?) = apply { this.company = company }
        fun privacyDetail(privacyDetail: PrivacyDetailDomain?) = apply { this.privacyDetail = privacyDetail }

        fun kbisFile(kbisFile: MetafileDomain?) = apply { this.kbisFile = kbisFile }
        fun ribFile(ribFile: MetafileDomain?) = apply { this.ribFile = ribFile }
        fun rcFile(rcFile: MetafileDomain?) = apply { this.rcFile = rcFile }
        fun urssafFile(urssafFile: MetafileDomain?) = apply { this.urssafFile = urssafFile }
        fun fiscalFile(fiscalFile: MetafileDomain?) = apply { this.fiscalFile = fiscalFile }

        fun state(state: FreelanceStateDomain?) = apply { this.state = state }

        fun build() = FreelanceDomain(
                username,
                contact!!,
                company!!,
                privacyDetail!!,
                kbisFile!!,
                ribFile!!,
                rcFile!!,
                urssafFile!!,
                fiscalFile!!,
                state!!
        )
    }
}

