package io.tricefal.core.freelance

import java.time.Instant

data class FreelanceDomain
    constructor(
        val username: String,
        var contact: ContactDomain?,
        var address: AddressDomain?,
        var company: CompanyDomain?,
        var privacyDetail: PrivacyDetailDomain?,

        var kbisFilename: String?,
        var ribFilename: String?,
        var rcFilename: String?,
        var urssafFilename: String?,
        var fiscalFilename: String?,

        var withMission: Boolean?,
        var availability: Availability?,
        var state: FreelanceStateDomain?,
        var lastDate: Instant?
    ) {

    data class Builder(
        val username: String,
        var contact: ContactDomain? = null,
        var address: AddressDomain? = null,
        var company: CompanyDomain? = null,
        var privacyDetail: PrivacyDetailDomain? = null,

        var kbisFilename: String? = null,
        var ribFilename: String? = null,
        var rcFilename: String? = null,
        var urssafFilename: String? = null,
        var fiscalFilename: String? = null,

        var withMission: Boolean? = null,
        var availability: Availability? = null,
        var state: FreelanceStateDomain? = null,
        var lastDate: Instant? = null

    ) {
        fun contact(contact: ContactDomain?) = apply { this.contact = contact ?: ContactDomain.Builder(username).build() }
        fun address(address: AddressDomain?) = apply { this.address = address ?: AddressDomain.Builder().build() }
        fun company(company: CompanyDomain?) = apply { this.company = company ?: CompanyDomain.Builder("......").build() }
        fun privacyDetail(privacyDetail: PrivacyDetailDomain?) = apply { this.privacyDetail = privacyDetail ?: PrivacyDetailDomain.Builder(username).build() }

        fun kbisFilename(kbisFilename: String?) = apply { this.kbisFilename = kbisFilename }
        fun ribFilename(ribFilename: String?) = apply { this.ribFilename = ribFilename }
        fun rcFilename(rcFilename: String?) = apply { this.rcFilename = rcFilename }
        fun urssafFilename(urssafFilename: String?) = apply { this.urssafFilename = urssafFilename }
        fun fiscalFilename(fiscalFilename: String?) = apply { this.fiscalFilename = fiscalFilename }

        fun withMission(withMission: Boolean?) = apply { this.withMission = withMission }
        fun availability(availability: Availability?) = apply { this.availability = availability }

        fun state(state: FreelanceStateDomain?) = apply { this.state = state ?: FreelanceStateDomain.Builder(username).build() }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = FreelanceDomain(
            username,
            contact,
            address,
            company,
            privacyDetail,
            kbisFilename,
            ribFilename,
            rcFilename,
            urssafFilename,
            fiscalFilename,
            withMission,
            availability,
            state,
            lastDate
        )
    }
}

enum class Availability {
    NONE,
    IN_MISSION,
    AVAILABLE_SOON,
    AVAILABLE,
    LOOKING;
}