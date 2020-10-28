package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain

interface IFreelanceService {
    fun create(freelance: FreelanceDomain) : FreelanceDomain
    fun findByUsername(username: String): FreelanceDomain
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun kbisUploaded(freelance: FreelanceDomain, kbisFileDomain: MetafileDomain): FreelanceDomain
    fun ribUploaded(freelance: FreelanceDomain, ribFileDomain: MetafileDomain): FreelanceDomain
    fun rcUploaded(freelance: FreelanceDomain, rcFileDomain: MetafileDomain): FreelanceDomain
    fun urssafUploaded(freelance: FreelanceDomain, urssafFileDomain: MetafileDomain): FreelanceDomain
    fun fiscalUploaded(freelance: FreelanceDomain, fiscalFileDomain: MetafileDomain): FreelanceDomain

}