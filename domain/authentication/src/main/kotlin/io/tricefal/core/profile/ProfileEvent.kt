package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.metafile.Representation

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
    fun isRef() : Boolean {
        return this.metafile.username.isNotBlank()
                && this.metafile.filename.isNotBlank()
                && this.metafile.representation == Representation.CV_LINKEDIN
    }
}