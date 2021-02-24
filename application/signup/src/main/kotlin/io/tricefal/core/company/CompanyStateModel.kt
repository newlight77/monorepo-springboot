package io.tricefal.core.company

class CompanyStateModel(
        val username: String,
        var kbisUploaded: Boolean? = null,
        var ribUploaded: Boolean? = null,
        var rcUploaded: Boolean? = null,
        var urssafUploaded: Boolean? = null,
        var fiscalUploaded: Boolean? = null,
        var completed: Boolean? = null
) {

    data class Builder (
            val username: String,
            var kbisUploaded: Boolean? = null,
            var ribUploaded: Boolean? = null,
            var rcUploaded: Boolean? = null,
            var urssafUploaded: Boolean? = null,
            var fiscalUploaded: Boolean? = null,
            var completed: Boolean? = null
    ) {
        fun kbisUploaded(kbisUploaded: Boolean?) = apply { this.kbisUploaded = kbisUploaded }
        fun ribUploaded(ribUploaded: Boolean?) = apply { this.ribUploaded = ribUploaded }
        fun rcUploaded(rcUploaded: Boolean?) = apply { this.rcUploaded = rcUploaded }
        fun urssafUploaded(urssafUploaded: Boolean?) = apply { this.urssafUploaded = urssafUploaded }
        fun fiscalUploaded(fiscalUploaded: Boolean?) = apply { this.fiscalUploaded = fiscalUploaded }
        fun completed(completed: Boolean?) = apply { this.completed = completed }

        fun build() = CompanyStateModel(
                username,
                kbisUploaded,
                ribUploaded,
                rcUploaded,
                urssafUploaded,
                fiscalUploaded,
                completed
        )
    }
}

fun toModel(domain: CompanyStateDomain): CompanyStateModel {
    return CompanyStateModel.Builder(domain.companyName)
            .kbisUploaded(domain.kbisUploaded)
            .ribUploaded(domain.ribUploaded)
            .rcUploaded(domain.rcUploaded)
            .urssafUploaded(domain.urssafUploaded)
            .fiscalUploaded(domain.fiscalUploaded)
            .completed(domain.completed)
            .build()
}

fun fromModel(model: CompanyStateModel): CompanyStateDomain {
    return CompanyStateDomain.Builder(model.username)
            .kbisUploaded(model.kbisUploaded)
            .ribUploaded(model.ribUploaded)
            .rcUploaded(model.rcUploaded)
            .urssafUploaded(model.urssafUploaded)
            .fiscalUploaded(model.fiscalUploaded)
            .completed(model.completed)
            .build()
}