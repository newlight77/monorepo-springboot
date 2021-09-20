package io.oneprofile.signup.company

import io.oneprofile.signup.notification.EmailNotificationDomain
import io.oneprofile.signup.notification.MetaNotificationDomain
import io.oneprofile.shared.util.json.JsonPatchOperator
import io.oneprofile.shared.util.json.PatchOperation
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

        val acceptedOps = operations.filter { acceptOperation(it) }
        val tobePatched = if (company.isPresent) company.get() else dataAdapter.create(createCompany(companyName))

        acceptedOps.parallelStream().forEach() { op ->
            createMissingChildForPatch(tobePatched, op)
        }

        val patched = applyPatch(tobePatched, acceptedOps)
        dataAdapter.update(companyName, patched)
        return patched
    }

    private fun applyPatch(
        company: CompanyDomain,
        operations: List<PatchOperation>,
    ): CompanyDomain {
        return operations.let { ops ->
            val patched = JsonPatchOperator().apply(company, ops)
            patched.lastDate = Instant.now()
            patched
        }
    }

    override fun completed(username: String, companyName: String, metaNotification: MetaNotificationDomain): CompanyDomain {
        val company = findByName(companyName)
        company.state?.completed = true
        company.lastDate = Instant.now()
        dataAdapter.update(companyName, company)
        sendEmail(username, company, emailNotification(username, company, metaNotification))
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
            logger.error("Failed to update the company from the kbis uploaded event for user $companyName", ex)
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
            logger.error("Failed to update the company from the rib uploaded event for user $companyName", ex)
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
            logger.error("Failed to update the company from the rc uploaded event for user $companyName", ex)
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
            logger.error("Failed to update the company from the urssaf uploaded event for user $companyName", ex)
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
            logger.error("Failed to update the company from the fiscal uploaded event for user $companyName", ex)
            throw FiscalFileUploadException("Failed to update the company from the fiscal uploaded event for user $companyName", ex)
        }
        return company
    }

    private fun sendEmail(username: String, company: CompanyDomain, companyCompletionNotification: EmailNotificationDomain): Boolean {
        try {
            dataAdapter.sendEmail(companyCompletionNotification)
            dataAdapter.companyCompleted(username, company.raisonSocial)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email upon company company completion for username ${company.raisonSocial}", ex)
            throw CompanyCompletionEmailNotificationException("failed to send an email upon company company completion for username ${company.raisonSocial}", ex)
        }
    }

    private fun emailNotification(username: String, company: CompanyDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("company.completed.email.subject")
        val emailGreeting = getString("company.completed.email.greeting", "admin")
        val emailContent = getString("company.completed.email.content")
        val emailSignature = getString("company.completed.email.signature")

        return EmailNotificationDomain.Builder(company.raisonSocial)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(username)
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


    private fun acceptOperation(operation: PatchOperation): Boolean {
        if (operation.op == "replace" && operation.path.startsWith("/state")) return true

        if (operation.op == "replace" && operation.path.startsWith("/pdgContact")) return true
        if (operation.op == "replace" && operation.path.startsWith("/pdgPrivacyDetail")) return true
        if (operation.op == "replace" && operation.path.startsWith("/adminContact")) return true
        if (operation.op == "replace" && operation.path.startsWith("/bankInfo")) return true
//        if (operation.op == "replace" && operation.path.startsWith("/bankInfo/address")) return true
        if (operation.op == "replace" && operation.path.startsWith("/fiscalAddress")) return true
        if (operation.op == "replace" && operation.path.startsWith("/motherCompany")) return true
        if (operation.op == "replace" && operation.path.startsWith("/documents")) return true

        if (operation.op == "replace" && operation.path == "/raisonSocial") return true
        if (operation.op == "replace" && operation.path == "/nomCommercial") return true
        if (operation.op == "replace" && operation.path == "/formeJuridique") return true
        if (operation.op == "replace" && operation.path == "/capital") return true
        if (operation.op == "replace" && operation.path == "/rcs") return true
        if (operation.op == "replace" && operation.path == "/siret") return true
        if (operation.op == "replace" && operation.path == "/numDuns") return true
        if (operation.op == "replace" && operation.path == "/numTva") return true
        if (operation.op == "replace" && operation.path == "/codeNaf") return true
        if (operation.op == "replace" && operation.path == "/companyCreationDate") return true
        return false
    }

    private fun createMissingChildForPatch(company: CompanyDomain, operation: PatchOperation): CompanyDomain {
        if (operation.op == "replace" && operation.path.startsWith("/state")) {
            if (company.state == null) company.state = CompanyStateDomain(company.raisonSocial)
        }
        if (operation.op == "replace" && operation.path.startsWith("/pdgContact")) {
            if (company.pdgContact == null) company.pdgContact = ContactDomain.Builder().lastDate(Instant.now()).build()
        }
        if (operation.op == "replace" && operation.path.startsWith("/pdgPrivacyDetail")) {
            if (company.pdgPrivacyDetail == null) company.pdgPrivacyDetail = PrivacyDetailDomain.Builder().lastDate(Instant.now()).build()
        }
        if (operation.op == "replace" && operation.path.startsWith("/adminContact")) {
            if (company.adminContact == null) company.adminContact = ContactDomain.Builder().lastDate(Instant.now()).build()
        }
        if (operation.op == "replace" && operation.path.startsWith("/bankInfo")) {
            if (company.bankInfo == null) company.bankInfo = BankInfoDomain.Builder().lastDate(Instant.now()).build()
            if (operation.op == "replace" && operation.path == "/bankInfo/address") {
                if (company.bankInfo?.address == null) company.bankInfo?.address = AddressDomain.Builder().lastDate(Instant.now()).build()
            }
        }
        if (operation.op == "replace" && operation.path.startsWith("/fiscalAddress")) {
            if (company.fiscalAddress == null) company.fiscalAddress = AddressDomain.Builder().lastDate(Instant.now()).build()
        }
        if (operation.op == "replace" && operation.path.startsWith("/motherCompany")) {
            if (company.motherCompany == null) company.motherCompany = MotherCompanyDomain()
        }
        if (operation.op == "replace" && operation.path.startsWith("/documents")) {
            if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
        }

        return company
    }

    //    private fun completeCompany(company: CompanyDomain): CompanyDomain {
//        if (company.pdgContact == null) company.pdgContact = ContactDomain.Builder().lastDate(Instant.now()).build()
//        if (company.pdgPrivacyDetail == null) company.pdgPrivacyDetail = PrivacyDetailDomain.Builder().lastDate(Instant.now()).build()
//        if (company.adminContact == null) company.adminContact = ContactDomain.Builder().lastDate(Instant.now()).build()
//        if (company.bankInfo == null) company.bankInfo = BankInfoDomain.Builder().lastDate(Instant.now()).build()
//        if (company.bankInfo?.address == null) company.bankInfo?.address = AddressDomain.Builder().lastDate(Instant.now()).build()
//        if (company.fiscalAddress == null) company.fiscalAddress = AddressDomain.Builder().lastDate(Instant.now()).build()
//        if (company.motherCompany == null) company.motherCompany = MotherCompanyDomain()
//        if (company.documents == null) company.documents = CompanyDocumentsDomain.Builder().build()
//        if (company.state == null) company.state = CompanyStateDomain(companyName = company.raisonSocial)
//        return company
//    }

    private fun createCompany(companyName: String): CompanyDomain {
//        val pdgContact = ContactDomain.Builder().email(username).build()
//        val pdgPrivacyDetail = PrivacyDetailDomain.Builder().build()
//        val adminContact = ContactDomain.Builder().email(username).build()
//        val bankInfo = BankInfoDomain.Builder().address(AddressDomain.Builder().build()).build()
//        val fiscalAddress = AddressDomain.Builder().build()
//        val motherCompany = MotherCompanyDomain()
//        val documents = CompanyDocumentsDomain.Builder().build()
        val state = CompanyStateDomain.Builder(companyName).build()
        return CompanyDomain.Builder(raisonSocial = "")
//            .pdgPrivacyDetail(pdgPrivacyDetail)
//            .pdgContact(pdgContact)
//            .adminContact(adminContact)
//            .bankInfo(bankInfo)
//            .fiscalAddress(fiscalAddress)
//            .motherCompany(motherCompany)
//            .documents(documents)
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