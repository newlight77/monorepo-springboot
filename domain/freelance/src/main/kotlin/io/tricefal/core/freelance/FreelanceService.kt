package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain
import java.util.*
import org.slf4j.LoggerFactory

class FreelanceService(private var adapter: IFreelanceAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        adapter.findByUsername(freelance.username).ifPresent {
            throw UsernameNotFoundException("a freelance with username ${freelance.username} is already taken")
        }

        return adapter.create(freelance)
    }

    override fun findByUsername(username: String): Optional<FreelanceDomain> {
        return adapter.findByUsername(username)
    }

    override fun findAll(): List<FreelanceDomain> {
        return adapter.findAll()
    }

    override fun resumeUploaded(freelance: FreelanceDomain, resumeFileDomain: MetafileDomain): FreelanceDomain {
        freelance.resumeFile = resumeFileDomain
        adapter.update(freelance)
        return freelance
    }

}

class UsernameNotFoundException(val s: String) : Throwable()
