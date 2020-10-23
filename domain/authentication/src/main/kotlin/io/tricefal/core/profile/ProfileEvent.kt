package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.metafile.Representation

class PortraitUploadedEvent(val portrait: MetafileDomain) {
    val username = portrait.username
    fun isPortrait() : Boolean {
        return this.portrait.username.isNotBlank()
                && this.portrait.filename.isNotBlank()
                && this.portrait.representation == Representation.PORTRAIT
    }
}

class ResumeUploadedEvent(var resume: MetafileDomain) {
    val username = resume.username
    fun isResume() : Boolean {
        return this.resume.username.isNotBlank()
                && this.resume.filename.isNotBlank()
                && this.resume.representation == Representation.CV
    }
}

class RefUploadedEvent(var ref: MetafileDomain) {
    val username = ref.username
    fun isRef() : Boolean {
        return this.ref.username.isNotBlank()
                && this.ref.filename.isNotBlank()
                && this.ref.representation == Representation.REF
    }
}