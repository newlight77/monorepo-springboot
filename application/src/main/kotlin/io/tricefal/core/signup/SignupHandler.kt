package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileModel
import io.tricefal.core.metafile.MetafileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*

@Service
class SignupHandler(val signupService: ISignupService, val metafileRepository: MetafileRepository) {

    @Value("{data.files.path}")
    lateinit var filesPath: String

    fun signup(signup: SignupModel): SignupResult {
        val signupDomain = signupService.signup(fromModel(signup))

        return SignupResult.Builder(signupDomain.username)
                .signup(toModel(signupDomain))
                .created(true)
                .emailSent(true)
                .activationCodeSent(true)
                .build()

        // create account on okta
        // send an email
        // send activation code via sms to phone number
        // create a SMS notification infra adapter
    }

    fun findByUsername(username: String): Optional<SignupModel> {
        return signupService.findByUsername(username).map { toModel(it) }
    }

    fun activate(username: String, code: String): SignupResult {
        TODO("Not yet implemented")
        // actrivation de compte
    }

    fun upload(username: String, file: MultipartFile): SignupModel {
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)

        if (fileName.contains("..")) {
            throw IllegalArgumentException("Sorry! Filename contains invalid path sequence $fileName")
        }

        val metafile = MetafileModel(0, username, fileName, file.contentType!!, Instant.now())
        val metafileDomain = io.tricefal.core.metafile.fromModel(metafile)
        metafileRepository.save(metafileDomain, file.inputStream)
        val signup = signupService.updateMetafile(username, metafileDomain)

        return toModel(signup)
    }

    fun updateStatus(username: String, status: String): SignupModel {
        val signup = signupService.updateStatus(username, Status.valueOf(status))
        // notifie michel et arnaud
        return toModel(signup)
    }

}