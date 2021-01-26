package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.metafile.Representation

class SignupStatusUpdatedEvent(val signup: SignupDomain) {
    fun isFreelance() = Status.FREELANCE === signup.status || Status.FREELANCE_WITH_MISSION === signup.status
    fun isEmployee() = Status.EMPLOYEE === signup.status
    fun isClient() = Status.CLIENT === signup.status
}

class SignupStateUpdatedEvent(val username: String, val state: String)

class CompanyCompletionEvent(val username: String)

class PortraitUploadedEvent(val metafile: MetafileDomain) {
    val username = metafile.username
    fun isPortrait() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.PORTRAIT
    }
}

class ResumeUploadedEvent(var metafile: MetafileDomain) {
    val username = metafile.username
    fun isResume() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV
    }
}

class ResumeLinkedinUploadedEvent(var metafile: MetafileDomain) {
    val username = metafile.username
    fun isResumeLinkedin() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV_LINKEDIN
    }
}

class MissionResumeUploadedEvent(var metafile: MetafileDomain) {
    val username = metafile.username
    fun isMissionResume() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV_MISSION
    }
}