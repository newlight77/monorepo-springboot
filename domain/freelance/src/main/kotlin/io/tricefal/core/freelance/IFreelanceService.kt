package io.tricefal.core.freelance

import io.tricefal.shared.util.PatchOperation
import java.util.*

interface IFreelanceService {
    fun create(freelance: FreelanceDomain) : FreelanceDomain
    fun update(username: String, freelance: FreelanceDomain) : FreelanceDomain
    fun patch(username: String, operations: List<PatchOperation>) : FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun updateOnKbisUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnRibUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnRcUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnUrssafUploaded(username: String, filename: String): FreelanceDomain
    fun updateOnFiscalUploaded(username: String, filename: String): FreelanceDomain

}