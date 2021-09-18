package io.oneprofile.core.freelance

import java.time.Instant

class FreelanceStateDomain(
        val username: String,
        var kbisUploaded: Boolean? = null,
        var ribUploaded: Boolean? = null,
        var rcUploaded: Boolean? = null,
        var urssafUploaded: Boolean? = null,
        var fiscalUploaded: Boolean? = null,
        var missionResumedUploaded: Boolean? = null,
        var completed: Boolean? = null,
        var lastDate: Instant? = null
) {

    data class Builder (
            val username: String,
            var kbisUploaded: Boolean? = null,
            var ribUploaded: Boolean? = null,
            var rcUploaded: Boolean? = null,
            var urssafUploaded: Boolean? = null,
            var fiscalUploaded: Boolean? = null,
            var missionResumedUploaded: Boolean? = null,
            var completed: Boolean? = null,
            var lastDate: Instant? = null
    ) {
        fun kbisUploaded(kbisUploaded: Boolean?) = apply { this.kbisUploaded = kbisUploaded }
        fun ribUploaded(ribUploaded: Boolean?) = apply { this.ribUploaded = ribUploaded }
        fun rcUploaded(rcUploaded: Boolean?) = apply { this.rcUploaded = rcUploaded }
        fun urssafUploaded(urssafUploaded: Boolean?) = apply { this.urssafUploaded = urssafUploaded }
        fun fiscalUploaded(fiscalUploaded: Boolean?) = apply { this.fiscalUploaded = fiscalUploaded }
        fun missionResumedUploaded(missionResumedUploaded: Boolean?) = apply { this.missionResumedUploaded = missionResumedUploaded }
        fun completed(completed: Boolean?) = apply { this.completed = completed }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = FreelanceStateDomain(
                username,
                kbisUploaded,
                ribUploaded,
                rcUploaded,
                urssafUploaded,
                fiscalUploaded,
                missionResumedUploaded,
                completed,
                lastDate
        )
    }
}
