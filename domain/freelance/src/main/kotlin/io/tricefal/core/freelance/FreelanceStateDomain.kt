package io.tricefal.core.freelance

class FreelanceStateDomain(
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

        fun build() = FreelanceStateDomain(
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
