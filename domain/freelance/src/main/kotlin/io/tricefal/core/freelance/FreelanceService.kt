package io.tricefal.core.freelance

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.time.Instant
import java.util.*

class FreelanceService(private var dataAdapter: FreelanceDataAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val resourceBundle = ResourceBundle.getBundle("i18n.company", Locale.FRANCE)

    override fun signupStatusUpdated(username: String, event: String): FreelanceDomain {
        val contact = ContactDomain.Builder().email(username).build()
        val adminContact = ContactDomain.Builder().email(username).build()
        val company = CompanyDomain.Builder().adminContact(adminContact).build()
        val privacyDetail =PrivacyDetailDomain.Builder(username).build()
        val state = FreelanceStateDomain.Builder(username).build()
        val domain = FreelanceDomain.Builder(username)
            .company(company)
            .contact(contact)
            .privacyDetail(privacyDetail)
            .state(state)
            .build()
        val result = dataAdapter.findByUsername(username)
        return if (result.isPresent) update(username, result.get())
        else  create(domain)
    }

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        val result = dataAdapter.findByUsername(freelance.username)
        return if (result.isPresent) {
            return dataAdapter.update(result.get())
                .orElseThrow { NotFoundException("Failed to update an non existing freelance for user ${freelance.username}") }
        } else {
            dataAdapter.create(freelance)
        }
    }

    override fun update(username: String, freelance: FreelanceDomain): FreelanceDomain {
        val result = dataAdapter.findByUsername(freelance.username).orElse(dataAdapter.create(freelance))
        return dataAdapter.update(result)
            .orElseThrow { NotFoundException("Failed to update an non existing freelance for user ${freelance.username}") }
    }

    override fun patch(username: String, operations: List<PatchOperation>): FreelanceDomain {
        val freelance = dataAdapter.findByUsername(username)
//        val ops = operations.filter { acceptOperation(it) }
        return dataAdapter.patch(freelance.get(), operations)
            .orElseThrow { NotFoundException("Failed to update an non existing freelance for user ${username}") }
    }

//    private fun acceptOperation(operation: PatchOperation): Boolean {
//        if (operation.op == "replace" && operation.path == "/contact") return true
//        if (operation.op == "replace" && operation.path == "/company") return true
//        if (operation.op == "replace" && operation.path == "/PrivacyDetailDomain") return true
//        return false
//    }

    override fun completed(username: String, metaNotification: MetaNotificationDomain): FreelanceDomain {
        val freelance = findByUsername(username)
        freelance.state?.completed = true
        dataAdapter.update(freelance)
        sendEmail(freelance, emailNotification(freelance, metaNotification))
        return freelance
    }

    override fun findByUsername(username: String): FreelanceDomain {
        val result = dataAdapter.findByUsername(username)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a freelance for user $username")
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
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
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
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
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
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
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
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
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
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.fiscalFilename = filename
                        it.state?.fiscalUploaded = true
                        freelance = dataAdapter.update(it)
                            .orElseThrow { NotFoundException("Failed to update the freelance from the fiscal uploaded event for user $username") }
                    },
                    {
                        freelance.state?.fiscalUploaded = true
                        freelance = dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the fiscal uploaded event for user $username")
            throw FiscalFileUploadException("Failed to update the freelance from the fiscal uploaded event for user $username", ex)
        }
        return freelance
    }

    private fun sendEmail(freelance: FreelanceDomain, companyCompletionNotification: EmailNotificationDomain): Boolean {
        try {
            dataAdapter.sendEmail(freelance.username, companyCompletionNotification)
            dataAdapter.companyCompleted(freelance.username)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email upon freelance company completion for username ${freelance.username}")
            throw FreelanceCompletionEmailNotificationException("failed to send an email upon freelance company completion for username ${freelance.username}", ex)
        }
    }

    private fun emailNotification(freelance: FreelanceDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("company.completion.mail.subject")
        val emailGreeting = getString("company.completion.mail.greeting", "admin")
        val emailContent = getString("company.completion.mail.content")

        return EmailNotificationDomain.Builder(freelance.username)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(metaNotification.emailAdmin)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
            .build()
    }

    private fun getString(key: String, vararg params: String?): String? {
        return try {
            MessageFormat.format(resourceBundle.getString(key), *params)
        } catch (ex: MissingResourceException) {
            throw ResourceBundleMissingKeyException("Failed to retrieve the value for key=$key in resource bundle i18n", ex)
        }
    }

    class FreelanceCompletionEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class ResourceBundleMissingKeyException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }

}

class DuplicateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class NotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class KbisFileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class RibFileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class RcFileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class UrssafFileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class FiscalFileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}