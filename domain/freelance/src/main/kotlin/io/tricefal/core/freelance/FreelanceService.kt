package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory

class FreelanceService(private var adapter: IFreelanceAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        adapter.findByUsername(freelance.username).ifPresent {
            throw UsernameNotFoundException("a freelance with username ${freelance.username} is already taken")
        }

        return adapter.create(freelance)
    }

    override fun findByUsername(username: String): FreelanceDomain {
        if (username.isEmpty()) throw UsernameNotFoundException("username is $username")
        return adapter.findByUsername(username)
                .orElseThrow { NotFoundException("resource not found for username $username") }
    }

    override fun findAll(): List<FreelanceDomain> {
        return adapter.findAll()
    }

    override fun kbisUploaded(freelance: FreelanceDomain, kbisFileDomain: MetafileDomain): FreelanceDomain {
        freelance.kbisFile = kbisFileDomain
        freelance.state!!.kbisUploaded = true
        adapter.update(freelance)
        return freelance
    }

    override fun ribUploaded(freelance: FreelanceDomain, ribFileDomain: MetafileDomain): FreelanceDomain {
        freelance.ribFile = ribFileDomain
        freelance.state!!.ribUploaded = true
        adapter.update(freelance)
        return freelance
    }

    override fun rcUploaded(freelance: FreelanceDomain, rcFileDomain: MetafileDomain): FreelanceDomain {
        freelance.rcFile = rcFileDomain
        freelance.state!!.rcUploaded = true
        adapter.update(freelance)
        return freelance
    }

    override fun urssafUploaded(freelance: FreelanceDomain, urssafFileDomain: MetafileDomain): FreelanceDomain {
        freelance.urssafFile = urssafFileDomain
        freelance.state!!.urssafUploaded = true
        adapter.update(freelance)
        return freelance
    }

    override fun fiscalUploaded(freelance: FreelanceDomain, fiscalFileDomain: MetafileDomain): FreelanceDomain {
        freelance.fiscalFile = fiscalFileDomain
        freelance.state!!.fiscalUploaded = true
        adapter.update(freelance)
        return freelance
    }

}

class NotFoundException(val s: String) : Throwable()
class UsernameNotFoundException(val s: String) : Throwable()
