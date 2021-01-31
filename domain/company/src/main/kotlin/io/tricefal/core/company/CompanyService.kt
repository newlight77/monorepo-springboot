package io.tricefal.core.company

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.JsonPatchOperator
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.time.Instant
import java.util.*

class CompanyService(private var dataAdapter: CompanyDataAdapter) : ICompanyService {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val resourceBundle = ResourceBundle.getBundle("i18n.company", Locale.FRANCE)

    override fun create(company: CompanyDomain): CompanyDomain {
        val result = dataAdapter.findByName(company.raisonSocial)
        company.lastDate = Instant.now()
        return if (result.isPresent) dataAdapter.update(result.get().raisonSocial, company)
        else dataAdapter.create(company)
    }

    override fun update(companyName: String, company: CompanyDomain): CompanyDomain {
        val result = dataAdapter.findByName(companyName)
        company.lastDate = Instant.now()
        if (result.isEmpty) dataAdapter.create(company)
        return dataAdapter.update(companyName, company)
    }

    override fun patch(companyName: String, operations: List<PatchOperation>): CompanyDomain {
        val company = dataAdapter.findByName(companyName)
        if (company.isEmpty){
            var newCompany = createCompany(companyName)
            newCompany = applyPatch(newCompany, operations)
            dataAdapter.create(newCompany)
            return newCompany
        }
        val patched = applyPatch(company.get(), operations)
        dataAdapter.update(companyName, patched)
        return patched
    }

    private fun applyPatch(
        company: CompanyDomain,
        operations: List<PatchOperation>,
    ): CompanyDomain {
        return operations.let { ops ->
            val patched = JsonPatchOperator().apply(completeCompany(company), ops)
            patched.lastDate = Instant.now()
            patched
        }
    }

    private fun completeCompany(company: CompanyDomain): CompanyDomain {
        if (company.pdgContact == null) company.pdgContact = ContactDomain.Builder().lastDate(Instant.now()).build()
        if (company.pdgPrivacyDetail == null) company.pdgPrivacyDetail = PrivacyDetailDomain.Builder().lastDate(Instant.now()).build()
        if (company.adminContact == null) company.adminContact = ContactDomain.Builder().lastDate(Instant.now()).build()
        if (company.bankInfo == null) company.bankInfo = BankInfoDomain.Builder().lastDate(Instant.now()).build()
        if (company.bankInfo?.address == null) company.bankInfo?.address = AddressDomain.Builder().lastDate(Instant.now()).build()
        if (company.fiscalAddress == null) company.fiscalAddress = AddressDomain.Builder().lastDate(Instant.now()).build()
        if (company.motherCompany == null) company.motherCompany = MotherCompanyDomain()
        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
        return company
    }

    override fun completed(companyName: String, metaNotification: MetaNotificationDomain): CompanyDomain {
        val company = findByName(companyName)
        company.state?.completed = true
        company.lastDate = Instant.now()
        dataAdapter.update(companyName, company)
        sendEmail(company, emailNotification(company, metaNotification))
        return company
    }

    override fun findByName(companyName: String): CompanyDomain {
        val result = dataAdapter.findByName(companyName)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a company for user $companyName")
    }

    override fun updateOnKbisUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .documents(CompanyDocumentsDomain.Builder().build())
            .state(CompanyStateDomain(companyName))
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        if (it.state == null) it.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (it.documents == null) it.documents = CompanyDocumentsDomain.Builder().build()
                        it.state?.kbisUploaded = true
                        it.documents?.kbisFilename = filename
                        it.documents?.kbisUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        company = dataAdapter.update(companyName, it)
                    },
                    {
                        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
                        company.state?.kbisUploaded = true
                        company.documents?.kbisFilename = filename
                        company.documents?.kbisUpdateDate = updateDate
                        dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the kbis uploaded event for user $companyName")
            throw KbisFileUploadException("Failed to update the company from the kbis uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnRibUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .documents(CompanyDocumentsDomain.Builder().build())
            .state(CompanyStateDomain(companyName))
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        if (it.state == null) it.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (it.documents == null) it.documents = CompanyDocumentsDomain.Builder().build()
                        it.state?.ribUploaded = true
                        it.documents?.ribFilename = filename
                        it.documents?.ribUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        company = dataAdapter.update(companyName, it)
                    },
                    {
                        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
                        company.state?.ribUploaded = true
                        company.documents?.ribFilename = filename
                        company.documents?.ribUpdateDate = updateDate
                        dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the rib uploaded event for user $companyName")
            throw RibFileUploadException("Failed to update the company from the rib uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnRcUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .documents(CompanyDocumentsDomain.Builder().build())
            .state(CompanyStateDomain(companyName))
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        if (it.state == null) it.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (it.documents == null) it.documents = CompanyDocumentsDomain.Builder().build()
                        it.state?.rcUploaded = true
                        it.documents?.rcFilename = filename
                        it.documents?.rcUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        company = dataAdapter.update(companyName, it)
                    },
                    {
                        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
                        company.state?.rcUploaded = true
                        company.documents?.rcFilename = filename
                        company.documents?.rcUpdateDate = updateDate
                        dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the rc uploaded event for user $companyName")
            throw RcFileUploadException("Failed to update the company from the rc uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnUrssafUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .documents(CompanyDocumentsDomain.Builder().build())
            .state(CompanyStateDomain(companyName))
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        if (it.state == null) it.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (it.documents == null) it.documents = CompanyDocumentsDomain.Builder().build()
                        it.state?.urssafUploaded = true
                        it.documents?.urssafFilename = filename
                        it.documents?.urssafUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        company = dataAdapter.update(companyName, it)
                    },
                    {
                        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
                        company.state?.urssafUploaded = true
                        company.documents?.urssafFilename = filename
                        company.documents?.urssafUpdateDate = updateDate
                        dataAdapter.create(company)
                    }
                )
        } catch (ex: Exception) {
            logger.error("Failed to update the company from the urssaf uploaded event for user $companyName")
            throw UrssafFileUploadException("Failed to update the company from the urssaf uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnFiscalUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .documents(CompanyDocumentsDomain.Builder().build())
            .state(CompanyStateDomain(companyName))
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        if (it.state == null) it.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (it.documents == null) it.documents = CompanyDocumentsDomain.Builder().build()
                        it.state?.fiscalUploaded = true
                        it.documents?.fiscalFilename = filename
                        it.documents?.fiscalUpdateDate = updateDate
                        it.lastDate = Instant.now()
                        company = dataAdapter.update(companyName, it)
                    },
                    {
                        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
                        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
                        company.state?.fiscalUploaded = true
                        company.documents?.fiscalFilename = filename
                        company.documents?.fiscalUpdateDate = updateDate
                        dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the fiscal uploaded event for user $companyName")
            throw FiscalFileUploadException("Failed to update the company from the fiscal uploaded event for user $companyName", ex)
        }
        return company
    }

    private fun sendEmail(company: CompanyDomain, companyCompletionNotification: EmailNotificationDomain): Boolean {
        try {
            dataAdapter.sendEmail(company.raisonSocial, companyCompletionNotification)
            dataAdapter.companyCompleted(company.raisonSocial)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email upon company company completion for username ${company.raisonSocial}")
            throw CompanyCompletionEmailNotificationException("failed to send an email upon company company completion for username ${company.raisonSocial}", ex)
        }
    }

    private fun emailNotification(company: CompanyDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("company.completed.email.subject")
        val emailGreeting = getString("company.completed.email.greeting", "admin")
        val emailContent = getString("company.completed.email.content")
        val emailSignature = getString("company.completed.email.signature")

        return EmailNotificationDomain.Builder(company.raisonSocial)
            .emailFrom(metaNotification.emailAdmin)
            .emailTo(metaNotification.emailAdmin)
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

    private fun createCompany(username: String): CompanyDomain {
        val pdgContact = ContactDomain.Builder().email(username).build()
        val pdgPrivacyDetail = PrivacyDetailDomain.Builder().build()
        val adminContact = ContactDomain.Builder().email(username).build()
        val bankInfo = BankInfoDomain.Builder().address(AddressDomain.Builder().build()).build()
        val fiscalAddress = AddressDomain.Builder().build()
        val motherCompany = MotherCompanyDomain()
        val documents = CompanyDocumentsDomain.Builder().build()
        val state = CompanyStateDomain.Builder(username).build()
        return CompanyDomain.Builder(raisonSocial = "")
            .pdgPrivacyDetail(pdgPrivacyDetail)
            .pdgContact(pdgContact)
            .adminContact(adminContact)
            .bankInfo(bankInfo)
            .fiscalAddress(fiscalAddress)
            .motherCompany(motherCompany)
            .documents(documents)
            .state(state)
            .build()
    }

    class CompanyCompletionEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
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