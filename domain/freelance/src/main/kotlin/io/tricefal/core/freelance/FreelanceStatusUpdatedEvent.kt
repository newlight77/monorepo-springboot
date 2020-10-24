package io.tricefal.core.freelance

class FreelanceStatusUpdatedEvent(val username: String, val status: String) {
    fun isFreelance() = "FREELANCE" === status
    fun isEmployee() = "EMPLOYEE" === status
    fun isClient() = "CLIENT" === status
}
