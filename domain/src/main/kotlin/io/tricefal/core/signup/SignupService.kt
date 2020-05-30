package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

class SignupService(private var repository: ISignupRepository) : ISignupService {
    override fun signup(signup: SignupDomain): SignupDomain {
        return repository.save(signup)
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        return repository.findByUsername(username)
    }

    override fun updateStatus(username: String, status: Status): SignupDomain {
        val signup = repository.findByUsername(username).get()
        signup.status = status
        return repository.update(signup)
    }

    override fun updateMetafile(username: String, metafile: MetafileDomain): SignupDomain {
        val signup = repository.findByUsername(username).get()
        signup.metafile = metafile
        return repository.update(signup)
    }
}