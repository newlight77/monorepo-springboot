package io.tricefal.core.company

import io.tricefal.core.freelance.*
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
        val result = dataAdapter.findByName(company.companyName)
        return if (result.isPresent) dataAdapter.update(result.get())
        else dataAdapter.create(company)
    }

    override fun update(companyName: String, company: CompanyDomain): CompanyDomain {
        val result = dataAdapter.findByName(companyName).orElse(dataAdapter.create(company))
        dataAdapter.update(result)
        return result
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
        dataAdapter.update(patched)
        return patched
    }

    private fun applyPatch(
        company: CompanyDomain,
        operations: List<PatchOperation>,
    ): CompanyDomain {
        if (company.adminContact == null) company.adminContact = ContactDomain.Builder().build()
        if (company.adminContact?.address == null) company.adminContact?.address = AddressDomain.Builder().build()
        if (company.bankInfo == null) company.bankInfo = BankInfoDomain.Builder().build()
        if (company.bankInfo?.address == null) company.bankInfo?.address = AddressDomain.Builder().build()
        if (company.state == null) company.state = CompanyStateDomain(companyName = company.companyName)

        return operations.let { ops ->
            val patched = JsonPatchOperator().apply(company, ops)
            patched.lastDate = company.lastDate ?: Instant.now()
            patched
        }
    }

    override fun completed(companyName: String, metaNotification: MetaNotificationDomain): CompanyDomain {
        val company = findByName(companyName)
        company.state?.completed = true
        dataAdapter.update(company)
        sendEmail(company, emailNotification(company, metaNotification))
        return company
    }

    override fun findByName(companyName: String): CompanyDomain {
        val result = dataAdapter.findByName(companyName)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a company for user $companyName")
    }

    override fun updateOnKbisUploaded(companyName: String, filename: String): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .kbisFilename(filename)
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        it.kbisFilename = filename
                        it.state?.kbisUploaded = true
                        company = dataAdapter.update(it)
                    },
                    {
                        company.state?.kbisUploaded = true
                        company = dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the kbis uploaded event for user $companyName")
            throw KbisFileUploadException("Failed to update the company from the kbis uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnRibUploaded(companyName: String, filename: String): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .ribFilename(filename)
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        it.ribFilename = filename
                        it.state?.ribUploaded = true
                        company = dataAdapter.update(it)
                    },
                    {
                        company.state?.ribUploaded = true
                        company = dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the rib uploaded event for user $companyName")
            throw RibFileUploadException("Failed to update the company from the rib uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnRcUploaded(companyName: String, filename: String): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .rcFilename(filename)
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        it.rcFilename = filename
                        it.state?.rcUploaded = true
                        company = dataAdapter.update(it)
                    },
                    {
                        company.state?.rcUploaded = true
                        company = dataAdapter.create(company)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the company from the rc uploaded event for user $companyName")
            throw RcFileUploadException("Failed to update the company from the rc uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnUrssafUploaded(companyName: String, filename: String): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .urssafFilename(filename)
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        it.urssafFilename = filename
                        it.state?.urssafUploaded = true
                        company = dataAdapter.update(it)
                    },
                    {
                        company.state?.urssafUploaded = true
                        company = dataAdapter.create(company)
                    }
                )
        } catch (ex: Exception) {
            logger.error("Failed to update the company from the urssaf uploaded event for user $companyName")
            throw UrssafFileUploadException("Failed to update the company from the urssaf uploaded event for user $companyName", ex)
        }
        return company
    }

    override fun updateOnFiscalUploaded(companyName: String, filename: String): CompanyDomain {
        var company = CompanyDomain.Builder(companyName)
            .fiscalFilename(filename)
            .lastDate(Instant.now())
            .build()
        try {
            this.dataAdapter.findByName(companyName)
                .ifPresentOrElse(
                    {
                        it.fiscalFilename = filename
                        it.state?.fiscalUploaded = true
                        company = dataAdapter.update(it)
                    },
                    {
                        company.state?.fiscalUploaded = true
                        company = dataAdapter.create(company)
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
            dataAdapter.sendEmail(company.companyName, companyCompletionNotification)
            dataAdapter.companyCompleted(company.companyName)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email upon company company completion for username ${company.companyName}")
            throw CompanyCompletionEmailNotificationException("failed to send an email upon company company completion for username ${company.companyName}", ex)
        }
    }

    private fun emailNotification(company: CompanyDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("company.completion.mail.subject")
        val emailGreeting = getString("company.completion.mail.greeting", "admin")
        val emailContent = getString("company.completion.mail.content")

        return EmailNotificationDomain.Builder(company.companyName)
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

    private fun createCompany(username: String): CompanyDomain {
        val adminAddress = AddressDomain.Builder().build()
        val adminContact = ContactDomain.Builder().email(username).address(adminAddress).build()
        val bankAddress = AddressDomain.Builder().build()
        val bankInfo = BankInfoDomain.Builder().address(bankAddress).build()
        val fiscalAddress = AddressDomain.Builder().build()
        val state = CompanyStateDomain.Builder(username).build()
        return CompanyDomain.Builder(username)
            .adminContact(adminContact)
            .bankInfo(bankInfo)
            .fiscalAddress(fiscalAddress)
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