package io.tricefal.core.company

import io.tricefal.core.freelance.CompanyDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.PatchOperation

interface ICompanyService {
    fun create(company: CompanyDomain) : CompanyDomain
    fun update(companyName: String, company: CompanyDomain) : CompanyDomain
    fun patch(companyName: String, operations: List<PatchOperation>) : CompanyDomain
    fun findByName(companyName: String): CompanyDomain

    fun updateOnKbisUploaded(companyName: String, filename: String): CompanyDomain
    fun updateOnRibUploaded(companyName: String, filename: String): CompanyDomain
    fun updateOnRcUploaded(companyName: String, filename: String): CompanyDomain
    fun updateOnUrssafUploaded(companyName: String, filename: String): CompanyDomain
    fun updateOnFiscalUploaded(companyName: String, filename: String): CompanyDomain
    fun completed(companyName: String, metaNotification: MetaNotificationDomain): CompanyDomain

}