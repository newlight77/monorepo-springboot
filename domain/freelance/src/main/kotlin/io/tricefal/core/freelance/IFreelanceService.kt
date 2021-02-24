package io.tricefal.core.freelance

import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.PatchOperation
import java.time.Instant
import java.util.*

interface IFreelanceService {
    fun create(freelance: FreelanceDomain) : FreelanceDomain
    fun update(username: String, freelance: FreelanceDomain) : FreelanceDomain
    fun patch(username: String, operations: List<PatchOperation>) : FreelanceDomain
    fun findByUsername(username: String): FreelanceDomain
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun updateOnKbisUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain
    fun updateOnRibUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain
    fun updateOnRcUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain
    fun updateOnUrssafUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain
    fun updateOnFiscalUploaded(username: String, filename: String, updateDate: Instant): FreelanceDomain
    fun completed(username: String, metaNotification: MetaNotificationDomain): FreelanceDomain

}