package io.oneprofile.signup.company

import io.oneprofile.signup.notification.EmailNotificationDomain
import java.util.*

interface CompanyDataAdapter {
    fun create(company: CompanyDomain): CompanyDomain
    fun findByName(companyName: String): Optional<CompanyDomain>
    fun findAll(): List<CompanyDomain>
    fun update(companyName: String, company: CompanyDomain): CompanyDomain
    fun sendEmail(notification: EmailNotificationDomain): Boolean
    fun companyCompleted(username: String, companyName: String)
}