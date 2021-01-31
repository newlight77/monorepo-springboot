package io.tricefal.core.company

import java.time.Instant

data class MotherCompanyDomain(
    var raisonSocial: String? = null,
    var typeEntreprise: String? = null,
    var capital: String? = null,
    var creationDate: Instant? = null
)