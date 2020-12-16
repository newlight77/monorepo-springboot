package io.tricefal.core.freelance

import io.tricefal.shared.util.JsonPatchOperator
import io.tricefal.shared.util.PatchOperation
import org.slf4j.LoggerFactory
import java.util.*

class FreelanceService(private var adapter: IFreelanceAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        val result = adapter.findByUsername(freelance.username)
        return if (result.isPresent) adapter.update(freelance)
        else adapter.create(freelance)
    }

    override fun update(username: String, freelance: FreelanceDomain): FreelanceDomain {
        val result = adapter.findByUsername(freelance.username)
        return if (result.isPresent) adapter.update(freelance)
        else throw NotFoundException("Failed to update an non existing freelance for user ${freelance.username}")
    }

    override fun patch(username: String, operations: List<PatchOperation>): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
                .build()
        try {
            adapter.findByUsername(username)
                    .ifPresent {
                        operations.filter { op ->
                            acceptOperation(op)
                        }.also{
                            ops ->
                            freelance = JsonPatchOperator().apply(it, ops)
                            freelance = adapter.update(freelance)
                        }
                    }
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the kbis uploaded event for user $username")
            throw KbisFileUploadException("Failed to update the freelance from the kbis uploaded event for user $username", ex)
        }
        return freelance
    }

    private fun acceptOperation(operation: PatchOperation): Boolean {
        if (operation.op == "replace" && operation.path == "/contact") return true
        if (operation.op == "replace" && operation.path == "/company") return true
        if (operation.op == "replace" && operation.path == "/PrivacyDetailDomain") return true
        return false
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
        var freelance = FreelanceDomain.Builder(username)
                .kbisFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.kbisFilename = filename
                                freelance = adapter.update(it)
                            },
                            {
                                freelance = adapter.create(freelance)
                            }
                    )
            freelance.state?.kbisUploaded = true
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
                                freelance = adapter.update(it)
                            },
                            {
                                freelance = adapter.create(freelance)
                            }
                    )
            freelance.state?.ribUploaded = true
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
                                freelance = adapter.update(it)
                            },
                            {
                                freelance = adapter.create(freelance)
                            }
                    )
            freelance.state?.rcUploaded = true
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
                                freelance = adapter.update(it)
                            },
                            {
                                freelance = adapter.create(freelance)
                            }
                    )
            freelance.state?.urssafUploaded = true
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
                                freelance = adapter.update(it)
                            },
                            {
                                freelance = adapter.create(freelance)
                            }
                    )
            freelance.state?.kbisUploaded = true
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