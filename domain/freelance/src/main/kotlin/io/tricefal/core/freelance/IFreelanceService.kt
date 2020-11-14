package io.tricefal.core.freelance

import java.util.*

interface IFreelanceService {
    fun create(freelance: FreelanceDomain) : FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun updateOnKbisUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnRibUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnRcUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnUrssafUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnFiscalUploaded(username: String, filename: String): FreelanceDomain

}