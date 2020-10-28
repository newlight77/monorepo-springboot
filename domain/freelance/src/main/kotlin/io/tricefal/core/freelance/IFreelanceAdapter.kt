package io.tricefal.core.freelance

import java.util.*

interface IFreelanceAdapter {
    fun create(freelance: FreelanceDomain): FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun update(freelance: FreelanceDomain): FreelanceDomain

}