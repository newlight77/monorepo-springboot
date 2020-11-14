package io.tricefal.core.freelance

import org.slf4j.LoggerFactory
import java.util.*

class FreelanceService(private var adapter: IFreelanceAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        val result = adapter.findByUsername(freelance.username)
        return if (result.isPresent) adapter.update(freelance)
        else adapter.create(freelance)
    }

    override fun findByUsername(username: String): Optional<FreelanceDomain> {
        return adapter.findByUsername(username)
    }

    override fun findAll(): List<FreelanceDomain> {
        return adapter.findAll()
    }

    override fun availables(): List<FreelanceDomain> {
        return adapter.availables()
    }

    override fun updateOnKbisUploaded(username: String, filename: String): FreelanceDomain {
        val freelance = FreelanceDomain.Builder(username)
                .kbisFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.kbisFilename = filename
                                create(freelance)
                            },
                            { create(freelance) }
                    )
            freelance.state?.kbisUploaded = true
        } catch (ex: Exception) {
            logger.error("Failed to update the freelancee from the kbis uploaded event for user $username")
            throw KbisFileUploadException("Failed to update the freelancee from the kbis uploaded event for user $username")
        }
        return freelance
    }

    override fun updateOnRibUploaded(username: String, filename: String): FreelanceDomain {
        val freelance = FreelanceDomain.Builder(username)
                .ribFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.ribFilename = filename
                                create(freelance)
                            },
                            { create(freelance) }
                    )
            freelance.state?.ribUploaded = true
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the rib uploaded event for user $username")
            throw RibFileUploadException("Failed to update the freelance from the rib uploaded event for user $username")
        }
        return freelance
    }

    override fun updateOnRcUploaded(username: String, filename: String): FreelanceDomain {
        val freelance = FreelanceDomain.Builder(username)
                .rcFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.rcFilename = filename
                                create(freelance)
                            },
                            { create(freelance) }
                    )
            freelance.state?.rcUploaded = true
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the rc uploaded event for user $username")
            throw RcFileUploadException("Failed to update the freelance from the rc uploaded event for user $username")
        }
        return freelance
    }

    override fun updateOnUrssafUploaded(username: String, filename: String): FreelanceDomain {
        val freelance = FreelanceDomain.Builder(username)
                .urssafFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.urssafFilename = filename
                                create(freelance)
                            },
                            { create(freelance) }
                    )
            freelance.state?.urssafUploaded = true
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the urssaf uploaded event for user $username")
            throw UrssafFileUploadException("Failed to update the freelance from the urssaf uploaded event for user $username")
        }
        return freelance
    }

    override fun updateOnFiscalUploaded(username: String, filename: String): FreelanceDomain {
        val freelance = FreelanceDomain.Builder(username)
                .fiscalFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.fiscalFilename = filename
                                create(freelance)
                            },
                            { create(freelance) }
                    )
            freelance.state?.kbisUploaded = true
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the fiscal uploaded event for user $username")
            throw FiscalFileUploadException("Failed to update the freelance from the fiscal uploaded event for user $username")
        }
        return freelance
    }

}

class NotFoundException(val s: String) : Throwable()
class UsernameNotFoundException(val s: String) : Throwable()
class KbisFileUploadException(val s: String) : Throwable()
class RibFileUploadException(val s: String) : Throwable()
class RcFileUploadException(val s: String) : Throwable()
class UrssafFileUploadException(val s: String) : Throwable()
class FiscalFileUploadException(val s: String) : Throwable()