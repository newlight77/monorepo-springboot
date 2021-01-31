package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.metafile.Representation

class SignupStatusUpdatedEvent(
    val username: String,
    val status: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?
) {
    fun isFreelance() = Status.FREELANCE.toString() === status || Status.FREELANCE_WITH_MISSION.toString() === status
    fun isEmployee() = Status.EMPLOYEE.toString() === status
    fun isClient() = Status.CLIENT.toString() === status
}

class SignupStateUpdatedEvent(val username: String, val state: String)

class CompanyCompletionEvent(val username: String)

class SignupResumeUploadedEvent(var metafile: MetafileDomain) {
    val username = metafile.username
    fun isResume() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV
    }
}

class SignupResumeLinkedinUploadedEvent(var metafile: MetafileDomain) {
    val username = metafile.username
    fun isResumeLinkedin() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV_LINKEDIN
    }
}

class ProfileResumeUploadedEvent(var username: String, var filename: String)
class ProfileResumeLinkedinUploadedEvent(var username: String, var filename: String)

class MissionResumeUploadedEvent(var metafile: MetafileDomain) {
    val username = metafile.username
    fun isMissionResume() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV_MISSION
    }
}