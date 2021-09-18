package io.oneprofile.core.freelance

import io.oneprofile.core.notification.EmailNotificationDomain
import java.util.*

interface FreelanceDataAdapter {
    fun create(freelance: FreelanceDomain): FreelanceDomain
    fun findByUsername(username: String): Optional<FreelanceDomain>
    fun findAll(): List<FreelanceDomain>
    fun availables(): List<FreelanceDomain>
    fun update(freelance: FreelanceDomain): FreelanceDomain
//    fun patch(freelance: FreelanceDomain, operations: List<PatchOperation>): Optional<FreelanceDomain>
    fun sendEmail(notification: EmailNotificationDomain): Boolean
    fun companyCompleted(username: String, companyName: String)
}