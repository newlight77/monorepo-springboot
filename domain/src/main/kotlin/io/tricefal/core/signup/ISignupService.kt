package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface ISignupService {
    fun signup(signup: SignupDomain)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun updateStatus(username: String, status: Status)
    fun updateMetafile(username: String, metafile: MetafileDomain)
}