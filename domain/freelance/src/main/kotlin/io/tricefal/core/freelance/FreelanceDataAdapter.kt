package io.tricefal.core.freelance

import io.tricefal.shared.util.json.PatchOperation
import java.util.*

interface FreelanceDataAdapter {
    fun create(freelance: FreelanceDomain): FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun update(freelance: FreelanceDomain): Optional<FreelanceDomain>
    fun patch(freelance: FreelanceDomain, operations: List<PatchOperation>): Optional<FreelanceDomain>
}