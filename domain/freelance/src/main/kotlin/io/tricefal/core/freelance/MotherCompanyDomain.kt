package io.tricefal.core.freelance

import java.time.Instant

data class MotherCompanyDomain(
    var raisonSocial: String? = null,
    var typeEntreprise: String? = null,
    var capital: String? = null,
    var creationDate: Instant? = null
)