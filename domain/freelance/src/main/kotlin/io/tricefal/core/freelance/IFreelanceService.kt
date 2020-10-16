package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface IFreelanceService {
    fun create(freelance: FreelanceDomain) : FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun resumeUploaded(freelance: FreelanceDomain, resumeFileDomain: MetafileDomain): FreelanceDomain

}