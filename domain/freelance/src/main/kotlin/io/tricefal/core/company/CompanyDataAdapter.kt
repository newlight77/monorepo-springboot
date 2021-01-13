package io.tricefal.core.company

import io.tricefal.core.freelance.CompanyDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.shared.util.json.PatchOperation
import java.util.*

interface CompanyDataAdapter {
    fun create(company: CompanyDomain): CompanyDomain
    fun findByName(companyName: String): Optional<CompanyDomain>
    fun findAll(): List<CompanyDomain>
    fun update(company: CompanyDomain): CompanyDomain
    fun sendEmail(companyName: String, companyCompletionNotification: EmailNotificationDomain): Boolean
    fun companyCompleted(companyName: String)
}