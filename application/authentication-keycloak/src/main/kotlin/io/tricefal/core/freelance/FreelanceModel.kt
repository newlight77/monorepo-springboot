package io.tricefal.core.freelance

import java.time.Instant


class FreelanceModel
    private constructor(
        val username: String,
        val contact: ContactModel?,
        val company: CompanyModel?,
        val privacyDetail: PrivacyDetailModel?,
        var status: Status?,
        var state: FreelanceStateModel?,
        var lastDate: Instant? = null
    ) {

    data class Builder (
            val username: String,
            var contact: ContactModel? = null,
            var company: CompanyModel? = null,
            var privacyDetail: PrivacyDetailModel? = null,
            var status: Status? = Status.NONE,
            var state: FreelanceStateModel? = null,
            var lastDate: Instant? = null
    ) {

        fun contact(contact: ContactModel?) = apply { this.contact = contact }
        fun company(company: CompanyModel?) = apply { this.company = company }
        fun privacyDetail(privacyDetail: PrivacyDetailModel?) = apply { this.privacyDetail = privacyDetail }
        fun status(status: Status?) = apply { this.status = status }
        fun state(state: FreelanceStateModel?) = apply {
            this.state = state ?: FreelanceStateModel.Builder(username).build()
        }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = FreelanceModel(
                username = username,
                contact = contact,
                company = company,
                privacyDetail = privacyDetail,
                status = status,
                state = state,
                lastDate = lastDate
        )
    }
}

fun toModel(domain: FreelanceDomain): FreelanceModel {
    return FreelanceModel.Builder(domain.username)
            .contact(domain.contact?.let { toModel(it) })
            .company(domain.company?.let { toModel(it) })
            .privacyDetail(domain.privacyDetail?.let { toModel(it) })
            .status(domain.status)
            .state(domain.state?.let { toModel(it) })
            .lastDate(domain.lastDate)
            .build()
}

fun fromModel(model: FreelanceModel): FreelanceDomain {
    return FreelanceDomain.Builder(model.username)
            .contact(model.contact?.let { fromModel(it) })
            .company(model.company?.let { fromModel(it) })
            .privacyDetail(model.privacyDetail?.let { fromModel(it) })
            .status(model.status)
            .state(model.state?.let { fromModel(it) })
            .lastDate(model.lastDate)
            .build()
}