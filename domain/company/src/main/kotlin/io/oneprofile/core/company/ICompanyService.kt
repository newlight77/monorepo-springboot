package io.oneprofile.core.company

import io.oneprofile.core.notification.MetaNotificationDomain
import io.oneprofile.shared.util.json.PatchOperation
import java.time.Instant

interface ICompanyService {
    fun create(company: CompanyDomain) : CompanyDomain
    fun update(companyName: String, company: CompanyDomain) : CompanyDomain
    fun patch(companyName: String, operations: List<PatchOperation>) : CompanyDomain
    fun findByName(companyName: String): CompanyDomain

    fun updateOnKbisUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain
    fun updateOnRibUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain
    fun updateOnRcUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain
    fun updateOnUrssafUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain
    fun updateOnFiscalUploaded(companyName: String, filename: String, updateDate: Instant): CompanyDomain
    fun completed(username: String, companyName: String, metaNotification: MetaNotificationDomain): CompanyDomain

}