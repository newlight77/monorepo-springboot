package io.tricefal.core.signup

import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.Representation
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import io.tricefal.core.notification.MetaNotificationDomain
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@PropertySource("classpath:application.yml", "classpath:twilio.yml", "classpath:keycloak.yml")
class SignupWebHandler(val signupService: ISignupService,
                       val metafileService: IMetafileService,
                       private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val dataFilesPath = env.getProperty("data.files.path")!!
    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    fun signup(signup: SignupModel): SignupStateModel {
        val domain = fromModel(signup)
        val result = signupService.signup(domain, MetaNotificationDomain(backendBaseUrl, emailFrom, smsFrom))
        logger.info("successfully signed up a new user ${signup.username}")
        return toModel(result)
    }

    fun delete(username: String, authorizationCode: String?) {
        val domain = signupService.findByUsername(username)
        this.signupService.delete(domain, authorizationCode)
        logger.info("successfully delete a signup for user $username")
    }

    fun findByUsername(username: String): SignupModel {
        return toModel(signupService.findByUsername(username))
    }

    fun findAll(): List<SignupModel> {
        return signupService.findAll().map { signupDomain -> toModel(signupDomain) }
    }

    fun activate(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)
        val model = this.signupService.activate(domain)
        logger.info("successfully activated signup for user $username")
        return toModel(model)
    }

    fun deactivate(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)
        val model = this.signupService.deactivate(domain)
        logger.info("successfully deactivated signup for user $username")
        return toModel(model)
    }

    fun resendCode(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val result = signupService.resendCode(domain, MetaNotificationDomain(backendBaseUrl, emailFrom, smsFrom))
        logger.info("successfully resent an activation code for user $username")
        return toModel(result)
    }

    fun verifyByCode(username: String, code: String): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val model = this.signupService.verifyByCode(domain, code)
        logger.info("successfully verified the activation code for user $username")
        return toModel(model)
    }

    fun state(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)

        return toModel(domain.state!!)
    }

    fun verifyByEmailFromToken(token: String): SignupStateModel {
        val model = this.signupService.verifyByCodeFromToken(token)
        logger.info("successfully verified the token by email")
        return toModel(model)
    }

    fun updateStatus(username: String, status: Status): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val model = this.signupService.updateStatus(domain, status)
        logger.info("successfully updated the status for user $username")
        return toModel(model)
    }

    fun uploadPortrait(username: String, file: MultipartFile): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.PORTRAIT))
        metafileService.save(metaFile, file.inputStream)

        val model = this.signupService.portraitUploaded(domain, metaFile)
        logger.info("successfully upload the portrait for user $username")
        return toModel(model)
    }

    fun uploadResume(username: String, file: MultipartFile): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV))
        metafileService.save(metaFile, file.inputStream)

        val model = this.signupService.resumeUploaded(domain, metaFile)
        logger.info("successfully upload the resume for user $username")
        return toModel(model)
    }

    fun uploadResumeLinkedin(username: String, file: MultipartFile): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV_LINKEDIN))
        metafileService.save(metaFile, file.inputStream)

        val model = this.signupService.resumeLinkedinUploaded(domain, metaFile)
        logger.info("successfully upload the resume linkedin for user $username")
        return toModel(model)
    }

}
