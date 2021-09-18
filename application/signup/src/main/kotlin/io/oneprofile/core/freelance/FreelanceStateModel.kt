package io.oneprofile.core.freelance

class FreelanceStateModel(
        val username: String,
        var kbisUploaded: Boolean? = null,
        var ribUploaded: Boolean? = null,
        var rcUploaded: Boolean? = null,
        var urssafUploaded: Boolean? = null,
        var fiscalUploaded: Boolean? = null,
        var missionResumedUploaded: Boolean? = null,
        var completed: Boolean? = null
) {

    data class Builder (
            val username: String,
            var kbisUploaded: Boolean? = null,
            var ribUploaded: Boolean? = null,
            var rcUploaded: Boolean? = null,
            var urssafUploaded: Boolean? = null,
            var fiscalUploaded: Boolean? = null,
            var missionResumedUploaded: Boolean? = null,
            var completed: Boolean? = null
    ) {
        fun kbisUploaded(kbisUploaded: Boolean?) = apply { this.kbisUploaded = kbisUploaded }
        fun ribUploaded(ribUploaded: Boolean?) = apply { this.ribUploaded = ribUploaded }
        fun rcUploaded(rcUploaded: Boolean?) = apply { this.rcUploaded = rcUploaded }
        fun urssafUploaded(urssafUploaded: Boolean?) = apply { this.urssafUploaded = urssafUploaded }
        fun fiscalUploaded(fiscalUploaded: Boolean?) = apply { this.fiscalUploaded = fiscalUploaded }
        fun missionResumedUploaded(missionResumedUploaded: Boolean?) = apply { this.missionResumedUploaded = missionResumedUploaded }
        fun completed(completed: Boolean?) = apply { this.completed = completed }

        fun build() = FreelanceStateModel(
                username,
                kbisUploaded,
                ribUploaded,
                rcUploaded,
                urssafUploaded,
                fiscalUploaded,
                missionResumedUploaded,
                completed
        )
    }
}

fun toModel(domain: FreelanceStateDomain): FreelanceStateModel {
    return FreelanceStateModel.Builder(domain.username)
            .kbisUploaded(domain.kbisUploaded)
            .ribUploaded(domain.ribUploaded)
            .rcUploaded(domain.rcUploaded)
            .urssafUploaded(domain.urssafUploaded)
            .fiscalUploaded(domain.fiscalUploaded)
            .missionResumedUploaded(domain.missionResumedUploaded)
            .completed(domain.completed)
            .build()
}

fun fromModel(model: FreelanceStateModel): FreelanceStateDomain {
    return FreelanceStateDomain.Builder(model.username)
            .kbisUploaded(model.kbisUploaded)
            .ribUploaded(model.ribUploaded)
            .rcUploaded(model.rcUploaded)
            .urssafUploaded(model.urssafUploaded)
            .fiscalUploaded(model.fiscalUploaded)
            .missionResumedUploaded(model.missionResumedUploaded)
            .completed(model.completed)
            .build()
}