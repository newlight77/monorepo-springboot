package io.tricefal.core.freelance

import io.tricefal.core.company.AddressModel
import io.tricefal.core.company.CompanyModel
import io.tricefal.core.company.ContactModel
import io.tricefal.core.company.PrivacyDetailModel
import java.time.Instant


class FreelanceModel
    private constructor(
        val username: String,
        var contact: ContactModel?,
        var address: AddressModel?,
        var company: CompanyModel?,
        var privacyDetail: PrivacyDetailModel?,
        var withMission: Boolean?,
        var availability: Availability?,
        var state: FreelanceStateModel?,
        var lastDate: Instant? = null
    ) {

    data class Builder (
        val username: String,
        var contact: ContactModel? = null,
        var address: AddressModel? = null,
        var company: CompanyModel? = null,
        var privacyDetail: PrivacyDetailModel? = null,
        var withMission: Boolean? = null,
        var availability: Availability? = Availability.NONE,
        var state: FreelanceStateModel? = null,
        var lastDate: Instant? = null
    ) {

        fun contact(contact: ContactModel?) = apply { this.contact = contact }
        fun address(address: AddressModel?) = apply { this.address = address }
        fun company(company: CompanyModel?) = apply { this.company = company }
        fun privacyDetail(privacyDetail: PrivacyDetailModel?) = apply { this.privacyDetail = privacyDetail }
        fun withMission(withMission: Boolean?) = apply { this.withMission = withMission }
        fun availability(availability: Availability?) = apply { this.availability = availability }
        fun state(state: FreelanceStateModel?) = apply {
            this.state = state ?: FreelanceStateModel.Builder(username).build()
        }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = FreelanceModel(
            username = username,
            contact = contact,
            address = address,
            company = company,
            privacyDetail = privacyDetail,
            withMission = withMission,
            availability = availability,
            state = state,
            lastDate = lastDate
        )
    }
}

fun toModel(domain: FreelanceDomain): FreelanceModel {
    return FreelanceModel.Builder(domain.username)
            .contact(domain.contact?.let { io.tricefal.core.company.toModel(it) })
            .address(domain.address?.let { io.tricefal.core.company.toModel(it) })
            .company(domain.company?.let { io.tricefal.core.company.toModel(it) })
            .privacyDetail(domain.privacyDetail?.let { io.tricefal.core.company.toModel(it) })
            .withMission(domain.withMission)
            .availability(domain.availability)
            .state(domain.state?.let { toModel(it) })
            .lastDate(domain.lastDate)
            .build()
}

fun fromModel(model: FreelanceModel): FreelanceDomain {
    return FreelanceDomain.Builder(model.username)
            .contact(model.contact?.let { io.tricefal.core.company.fromModel(it) })
            .address(model.address?.let { io.tricefal.core.company.fromModel(it) })
            .company(model.company?.let { io.tricefal.core.company.fromModel(it) })
            .privacyDetail(model.privacyDetail?.let { io.tricefal.core.company.fromModel(it) })
            .withMission(model.withMission)
            .availability(model.availability)
            .state(model.state?.let { fromModel(it) })
            .lastDate(model.lastDate)
            .build()
}