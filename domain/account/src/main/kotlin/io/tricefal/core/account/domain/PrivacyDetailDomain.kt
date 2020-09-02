package io.tricefal.core.account.domain

import java.time.LocalDate

class PrivacyDetailDomain(
        var id: Long,
        val birthDate: LocalDate,
        val birthCity: String,
        val birthCountry: String,
        val citizenship: String,
        val socialSecurityNumber: String,
        val information: String,
        val cinUrl: String,
        val socialSecurityUrl: String,
        val visaUrl: String,
        val diplomaUrl: String,
        val casierJudiciaireUrl: String
) {


}