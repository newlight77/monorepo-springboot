package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface IFreelanceService {
    fun create(freelance: FreelanceDomain) : FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun kbisUploaded(freelance: FreelanceDomain, kbisFileDomain: MetafileDomain): FreelanceDomain
    fun ribUploaded(freelance: FreelanceDomain, ribFileDomain: MetafileDomain): FreelanceDomain
    fun rcUploaded(freelance: FreelanceDomain, rcFileDomain: MetafileDomain): FreelanceDomain
    fun urssafUploaded(freelance: FreelanceDomain, urssafFileDomain: MetafileDomain): FreelanceDomain
    fun fiscalUploaded(freelance: FreelanceDomain, fiscalFileDomain: MetafileDomain): FreelanceDomain

}