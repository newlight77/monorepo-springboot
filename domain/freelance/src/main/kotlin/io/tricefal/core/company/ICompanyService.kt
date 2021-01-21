package io.tricefal.core.company

import io.tricefal.core.freelance.CompanyDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.PatchOperation
import java.time.Instant

interface ICompanyService {
    fun create(company: CompanyDomain) : CompanyDomain
    fun update(companyName: String, company: CompanyDomain) : CompanyDomain
    fun patch(companyName: String, operations: List<PatchOperation>) : CompanyDomain
    fun findByName(companyName: String): CompanyDomain

    fun updateOnKbisUploaded(companyName: String): CompanyDomain
    fun updateOnRibUploaded(companyName: String): CompanyDomain
    fun updateOnRcUploaded(companyName: String): CompanyDomain
    fun updateOnUrssafUploaded(companyName: String): CompanyDomain
    fun updateOnFiscalUploaded(companyName: String): CompanyDomain
    fun completed(companyName: String, metaNotification: MetaNotificationDomain): CompanyDomain

}