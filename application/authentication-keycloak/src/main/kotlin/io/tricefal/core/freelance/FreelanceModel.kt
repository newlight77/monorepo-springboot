package io.tricefal.core.freelance


class FreelanceModel
    private constructor(
        val username: String,
        val contact: ContactModel?,
        val company: CompanyModel?,
        val privacyDetail: PrivacyDetailModel?,
        var status: Status?,
        var state: FreelanceStateModel?
    ) {

    data class Builder (
            val username: String,
            var contact: ContactModel? = null,
            var company: CompanyModel? = null,
            var privacyDetail: PrivacyDetailModel? = null,
            var status: Status? = Status.NONE,
            var state: FreelanceStateModel? = null
    ) {

        fun contact(contact: ContactModel?) = apply { this.contact = contact }
        fun company(company: CompanyModel?) = apply { this.company = company }
        fun privacyDetail(privacyDetail: PrivacyDetailModel?) = apply { this.privacyDetail = privacyDetail }
        fun status(status: Status?) = apply { this.status = status }
        fun state(state: FreelanceStateModel?) = apply {
            this.state = state ?: FreelanceStateModel.Builder(username).build()
        }

        fun build() = FreelanceModel(
                username = username,
                contact = contact,
                company = company,
                privacyDetail = privacyDetail,
                status = status,
                state = state
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
            .build()
}

fun fromModel(model: FreelanceModel): FreelanceDomain {
    return FreelanceDomain.Builder(model.username)
            .contact(model.contact?.let { fromModel(it) })
            .company(model.company?.let { fromModel(it) })
            .privacyDetail(model.privacyDetail?.let { fromModel(it) })
            .status(model.status)
            .state(model.state?.let { fromModel(it) })
            .build()
}