package io.oneprofile.core.company

import java.time.Instant

class CompanyStateDomain(
        val companyName: String,
        var kbisUploaded: Boolean? = null,
        var ribUploaded: Boolean? = null,
        var rcUploaded: Boolean? = null,
        var urssafUploaded: Boolean? = null,
        var fiscalUploaded: Boolean? = null,
        var completed: Boolean? = null,
        var lastDate: Instant? = null
) {

    data class Builder (
            val companyName: String,
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
        fun completed(completed: Boolean?) = apply { this.completed = completed }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = CompanyStateDomain(
            companyName = companyName,
            kbisUploaded = kbisUploaded,
            ribUploaded = ribUploaded,
            rcUploaded = rcUploaded,
            urssafUploaded = urssafUploaded,
            fiscalUploaded = fiscalUploaded,
            completed = completed,
            lastDate = lastDate
        )
    }
}
