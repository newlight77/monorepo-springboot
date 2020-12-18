package io.tricefal.core.freelance

import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import java.util.*

class FreelanceService(private var dataAdapter: FreelanceDataAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        val result = dataAdapter.findByUsername(freelance.username)
        return if (result.isPresent)
            dataAdapter.update(freelance)
                .orElseThrow { NotFoundException("freelance already existed, and failed to update for user ${freelance.username}") }
        else dataAdapter.create(freelance)
    }

    override fun update(username: String, freelance: FreelanceDomain): FreelanceDomain {
        val result = dataAdapter.findByUsername(freelance.username)
        return if (result.isPresent)
                dataAdapter.update(freelance)
                    .orElseThrow { NotFoundException("Failed to update an non existing freelance for user ${freelance.username}") }
            else throw NotFoundException("Failed to update an non existing freelance for user ${freelance.username}")
    }

    override fun patch(username: String, operations: List<PatchOperation>): FreelanceDomain {
        val freelance = dataAdapter.findByUsername(username)
//        val ops = operations.filter { acceptOperation(it) }
        return if (freelance.isPresent)
                dataAdapter.patch(freelance.get(), operations)
                    .orElseThrow { NotFoundException("Failed to update an non existing freelance for user ${username}") }
            else throw NotFoundException("Failed to update an non existing freelance for user ${username}")
    }

//    private fun acceptOperation(operation: PatchOperation): Boolean {
//        if (operation.op == "replace" && operation.path == "/contact") return true
//        if (operation.op == "replace" && operation.path == "/company") return true
//        if (operation.op == "replace" && operation.path == "/PrivacyDetailDomain") return true
//        return false
//    }

    override fun findByUsername(username: String): Optional<FreelanceDomain> {
        return dataAdapter.findByUsername(username)
    }

    override fun findAll(): List<FreelanceDomain> {
        return dataAdapter.findAll()
    }

    override fun availables(): List<FreelanceDomain> {
        return dataAdapter.availables()
    }

    override fun updateOnKbisUploaded(username: String, filename: String): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
            .kbisFilename(filename)
            .build()
        try {
            this.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.kbisFilename = filename
                        it.state?.kbisUploaded = true
                        freelance = dataAdapter.update(it)
                            .orElseThrow { NotFoundException("Failed to update the freelance from the kbis uploaded event for user $username") }
                    },
                    {
                        freelance.state?.kbisUploaded = true
                        freelance = dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the kbis uploaded event for user $username")
            throw KbisFileUploadException("Failed to update the freelance from the kbis uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnRibUploaded(username: String, filename: String): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
                .ribFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.ribFilename = filename
                        it.state?.ribUploaded = true
                        freelance = dataAdapter.update(it)
                            .orElseThrow { NotFoundException("Failed to update the freelance from the Rib uploaded event for user $username") }
                    },
                    {
                        freelance.state?.ribUploaded = true
                        freelance = dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the rib uploaded event for user $username")
            throw RibFileUploadException("Failed to update the freelance from the rib uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnRcUploaded(username: String, filename: String): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
                .rcFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.rcFilename = filename
                        it.state?.rcUploaded = true
                        freelance = dataAdapter.update(it)
                            .orElseThrow { NotFoundException("Failed to update the freelance from the Rc uploaded event for user $username") }
                    },
                    {
                        freelance.state?.rcUploaded = true
                        freelance = dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the rc uploaded event for user $username")
            throw RcFileUploadException("Failed to update the freelance from the rc uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnUrssafUploaded(username: String, filename: String): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
                .urssafFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.urssafFilename = filename
                        it.state?.urssafUploaded = true
                        freelance = dataAdapter.update(it)
                            .orElseThrow { NotFoundException("Failed to update the freelance from the urssaf uploaded event for user $username") }
                    },
                    {
                        freelance.state?.urssafUploaded = true
                        freelance = dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the urssaf uploaded event for user $username")
            throw UrssafFileUploadException("Failed to update the freelance from the urssaf uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnFiscalUploaded(username: String, filename: String): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
                .fiscalFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.fiscalFilename = filename
                        it.state?.kbisUploaded = true
                        freelance = dataAdapter.update(it)
                            .orElseThrow { NotFoundException("Failed to update the freelance from the fiscal uploaded event for user $username") }
                    },
                    {
                        freelance.state?.kbisUploaded = true
                        freelance = dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the fiscal uploaded event for user $username")
            throw FiscalFileUploadException("Failed to update the freelance from the fiscal uploaded event for user $username", ex)
        }
        return freelance
    }

}

class NotFoundException(val s: String) : Throwable()
class KbisFileUploadException(val s: String, ex: Throwable) : Throwable(ex)
class RibFileUploadException(val s: String, ex: Throwable) : Throwable(ex)
class RcFileUploadException(val s: String, ex: Throwable) : Throwable(ex)
class UrssafFileUploadException(val s: String, ex: Throwable) : Throwable(ex)
class FiscalFileUploadException(val s: String, ex: Throwable) : Throwable(ex)