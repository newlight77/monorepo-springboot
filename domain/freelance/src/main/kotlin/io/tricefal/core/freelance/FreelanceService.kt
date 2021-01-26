package io.tricefal.core.freelance

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.JsonPatchOperator
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.time.Instant
import java.util.*

class FreelanceService(private var dataAdapter: FreelanceDataAdapter) : IFreelanceService {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val resourceBundle = ResourceBundle.getBundle("i18n.company", Locale.FRANCE)

    override fun signupStatusUpdated(freelance: FreelanceDomain): FreelanceDomain {
        val result = dataAdapter.findByUsername(freelance.username)
        return if (result.isEmpty) dataAdapter.create(completeFreelance(freelance))
        else result.get()
    }

    override fun create(freelance: FreelanceDomain): FreelanceDomain {
        val result = dataAdapter.findByUsername(freelance.username)
        return if (result.isPresent) {
            freelance.lastDate = Instant.now()
            freelance.company?.lastDate = Instant.now()
            return dataAdapter.update(freelance)
        } else {
            freelance.lastDate = Instant.now()
            freelance.company?.lastDate = Instant.now()
            dataAdapter.create(freelance)
        }
    }

    override fun update(username: String, freelance: FreelanceDomain): FreelanceDomain {
        if (username != freelance.username) throw NotFoundException("Failed to find a freelance for user $username")
        val result = dataAdapter.findByUsername(freelance.username)
        return if (result.isPresent) {
            freelance.lastDate = Instant.now()
            freelance.company?.lastDate = Instant.now()
            return dataAdapter.update(freelance)
        } else {
            freelance.lastDate = Instant.now()
            freelance.company?.lastDate = Instant.now()
            dataAdapter.create(freelance)
        }
    }

    override fun patch(username: String, operations: List<PatchOperation>): FreelanceDomain {
        val freelance = dataAdapter.findByUsername(username)
//        val ops = operations.filter { acceptOperation(it) }
        if (freelance.isEmpty){
            var newFreelance = completeFreelance(freelance.get())
            newFreelance = applyPatch(newFreelance, operations)
            dataAdapter.create(newFreelance)
            return newFreelance
        }
        val patched = applyPatch(freelance.get(), operations)
        dataAdapter.update(patched)
        return patched

//        return dataAdapter.patch(freelance, operations)
//                .orElseThrow { NotFoundException("Failed to update an non existing freelance for user $username") }
    }

    private fun applyPatch(
        freelance: FreelanceDomain,
        operations: List<PatchOperation>,
    ): FreelanceDomain {
        return operations.let { ops ->
            val patched = JsonPatchOperator().apply(completeFreelance(freelance), ops)
            patched.lastDate = Instant.now()
            patched.company?.lastDate = Instant.now()
            patched
        }
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
        freelance.company?.lastDate = Instant.now()
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

    override fun updateOnKbisUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.state?.kbisUploaded = true
                        it.company?.documents?.kbisFilename = filename
                        it.company?.documents?.kbisUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        freelance = dataAdapter.update(it)
                    },
                    {
                        freelance.state?.kbisUploaded = true
                        freelance.company?.documents?.kbisFilename = filename
                        freelance.company?.documents?.kbisUpdateDate = updateDate
                        dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the kbis uploaded event for user $username")
            throw KbisFileUploadException("Failed to update the freelance from the kbis uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnRibUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.state?.ribUploaded = true
                        it.company?.documents?.ribFilename = filename
                        it.company?.documents?.ribUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        freelance = dataAdapter.update(it)
                    },
                    {
                        freelance.state?.ribUploaded = true
                        freelance.company?.documents?.ribFilename = filename
                        freelance.company?.documents?.ribUpdateDate = updateDate
                        dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the rib uploaded event for user $username")
            throw RibFileUploadException("Failed to update the freelance from the rib uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnRcUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.state?.rcUploaded = true
                        it.company?.documents?.rcFilename = filename
                        it.company?.documents?.rcUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        freelance = dataAdapter.update(it)
                    },
                    {
                        freelance.state?.rcUploaded = true
                        freelance.company?.documents?.rcFilename = filename
                        freelance.company?.documents?.rcUpdateDate = updateDate
                        dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the freelance from the rc uploaded event for user $username")
            throw RcFileUploadException("Failed to update the freelance from the rc uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnUrssafUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.state?.urssafUploaded = true
                        it.company?.documents?.urssafFilename = filename
                        it.company?.documents?.urssafUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        freelance = dataAdapter.update(it)
                    },
                    {
                        freelance.state?.urssafUploaded = true
                        freelance.company?.documents?.urssafFilename = filename
                        freelance.company?.documents?.urssafUpdateDate = updateDate
                        dataAdapter.create(freelance)
                    }
                )
        } catch (ex: Exception) {
            logger.error("Failed to update the freelance from the urssaf uploaded event for user $username")
            throw UrssafFileUploadException("Failed to update the freelance from the urssaf uploaded event for user $username", ex)
        }
        return freelance
    }

    override fun updateOnFiscalUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain {
        var freelance = FreelanceDomain.Builder(username)
            .state(FreelanceStateDomain.Builder(username).build())
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.state?.fiscalUploaded = true
                        it.company?.documents?.fiscalFilename = filename
                        it.company?.documents?.fiscalUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        freelance = dataAdapter.update(it)
                    },
                    {
                        freelance.state?.fiscalUploaded = true
                        freelance.company?.documents?.fiscalFilename = filename
                        freelance.company?.documents?.fiscalUpdateDate = updateDate
                        dataAdapter.create(freelance)
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
        val emailSubject = getString("company.completed.email.subject")
        val emailGreeting = getString("company.completed.email.greeting", freelance.contact?.firstName)
        val emailContent = getString("company.completed.email.content")
        val emailSignature = getString("company.completed.email.signature")

        return EmailNotificationDomain.Builder(freelance.username)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(freelance.username)
            .emailBcc(metaNotification.emailAdmin)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
            .emailSignature(emailSignature)
            .targetEnv(metaNotification.targetEnv)
            .build()
    }

    private fun getString(key: String, vararg params: String?): String? {
        return try {
            MessageFormat.format(resourceBundle.getString(key), *params)
        } catch (ex: MissingResourceException) {
            throw ResourceBundleMissingKeyException("Failed to retrieve the value for key=$key in resource bundle i18n", ex)
        }
    }

    private fun completeFreelance(freelance: FreelanceDomain): FreelanceDomain {
        if (freelance.company == null) freelance.company = CompanyDomain.Builder("").build()
        if (freelance.company?.pdgContact == null) freelance.company?.pdgContact = ContactDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.company?.pdgPrivacyDetail == null) freelance.company?.pdgPrivacyDetail = PrivacyDetailDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.company?.adminContact == null) freelance.company?.adminContact = ContactDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.company?.bankInfo == null) freelance.company?.bankInfo = BankInfoDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.company?.bankInfo?.address == null) freelance.company?.bankInfo?.address = AddressDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.company?.fiscalAddress == null) freelance.company?.fiscalAddress = AddressDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.company?.motherCompany == null) freelance.company?.motherCompany = MotherCompanyDomain()
        if (freelance.company?.documents == null) freelance.company?.documents = CompanyDocumentsDomain.Builder().build()

        if (freelance.address == null) freelance.address = AddressDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.contact == null) freelance.contact = ContactDomain.Builder().email(freelance.username).lastDate(Instant.now()).build()
        if (freelance.privacyDetail == null) freelance.privacyDetail = PrivacyDetailDomain.Builder().lastDate(Instant.now()).build()
        if (freelance.state == null) freelance.state = FreelanceStateDomain(username = freelance.username)

        return freelance
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