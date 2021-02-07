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
    private var targetEnv = env.getProperty("target.env")!!
    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var emailAdmin = env.getProperty("notification.mail.admin")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!
    private var smsAdmin = env.getProperty("notification.sms.admin")!!

    fun signup(signup: SignupModel): SignupStateModel {
        val domain = fromModel(signup)
        val metaNotification = MetaNotificationDomain(
            targetEnv=targetEnv, baseUrl=backendBaseUrl,
            emailFrom=emailFrom, emailAdmin=emailAdmin,
            smsFrom=smsFrom, smsAdminNumber=smsAdmin)
        val result = signupService.signup(domain, metaNotification)
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
        return signupService.findAll()
            .map { signupDomain -> toModel(signupDomain) }
            .sortedByDescending { it.signupDate }
    }

    fun activate(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)
        val metaNotification = MetaNotificationDomain(
            targetEnv=targetEnv, baseUrl=backendBaseUrl,
            emailFrom=emailFrom, emailAdmin=emailAdmin,
            smsFrom=smsFrom, smsAdminNumber=smsAdmin)
        val model = this.signupService.activate(domain, metaNotification)
        logger.info("successfully activated signup for user $username")
        return toModel(model)
    }

    fun deactivate(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)
        val model = this.signupService.deactivate(domain)
        logger.info("successfully deactivated signup for user $username")
        return toModel(model)
    }

    fun resendCodeForValidation(username: String): Boolean {
        val domain = signupService.findByUsername(username)
        val metaNotification = MetaNotificationDomain(
            targetEnv=targetEnv, baseUrl=backendBaseUrl,
            emailFrom=emailFrom, emailAdmin=emailAdmin,
            smsFrom=smsFrom, smsAdminNumber=smsAdmin)
        val result = signupService.resendCodeBySmsForValidation(domain, metaNotification)
        logger.info("successfully resent an activation code for user $username")
        return result
    }

    fun resendEmailForValidation(username: String): Boolean {
        val domain = signupService.findByUsername(username)
        val metaNotification = MetaNotificationDomain(
            targetEnv=targetEnv, baseUrl=backendBaseUrl,
            emailFrom=emailFrom, emailAdmin=emailAdmin,
            smsFrom=smsFrom, smsAdminNumber=smsAdmin)
        val result = signupService.resendCodeByEmailForValidation(domain, metaNotification)
        logger.info("successfully resent an email with validation link for user $username")
        return result
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
        val metaNotification = MetaNotificationDomain(
            targetEnv=targetEnv, baseUrl=backendBaseUrl,
            emailFrom=emailFrom, emailAdmin=emailAdmin,
            smsFrom=smsFrom, smsAdminNumber=smsAdmin)
        val model = this.signupService.updateStatus(domain, status, metaNotification)
        logger.info("successfully updated the status for user $username")
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

    fun addComment(username: String, comment: CommentModel): CommentModel {
        val comment = this.signupService.addComment(username, fromModel(comment))
        logger.info("successfully verified the activation code for user $username")
        return toModel(comment)

    }

}
