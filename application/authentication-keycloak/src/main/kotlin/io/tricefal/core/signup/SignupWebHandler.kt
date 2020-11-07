package io.tricefal.core.signup

import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.Representation
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import io.tricefal.core.notification.NotificationDomain
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import java.util.*


@Service
@PropertySource("classpath:application.yml", "classpath:twilio.yml", "classpath:keycloak.yml")
class SignupWebHandler(val signupService: ISignupService,
                       val metafileService: IMetafileService,
                       private final val env: Environment,
                       private final val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val dataFilesPath = env.getProperty("data.files.path")!!
    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    val locale: Locale = LocaleContextHolder.getLocale()

    fun signup(signup: SignupModel): SignupStateModel {
        val domain = fromModel(signup)
        val result = try {
                signupService.signup(domain, notification(domain))
            } catch (ex: Exception) {
                logger.error("Failed to signup a new user ${signup.username}")
                throw SignupRegistrationException("Failed to signup a new user ${signup.username}")
            }
        logger.info("successfully signed up a new user ${signup.username}")
        return toModel(result)
    }

    fun delete(username: String, authorizationCode: String?) {
        try {
            val domain = signupService.findByUsername(username)
            this.signupService.delete(domain, authorizationCode)
        } catch (ex: Exception) {
            logger.error("Failed to delete a signup with username $username")
            throw SignupDeleteException("Failed to delete a signup with username $username")
        }
        logger.info("successfully delete a signup for user $username")
    }

    fun findByUsername(username: String): SignupModel {
        if (username.isEmpty()) throw SignupUserNotFoundException("username is $username")
        return toModel(signupService.findByUsername(username))
    }

    fun findAll(): List<SignupModel> {
        return signupService.findAll().map { signupDomain -> toModel(signupDomain) }
    }

    fun activate(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)
        val model = try {
            this.signupService.activate(domain)
        } catch (ex: Exception) {
            logger.error("Failed to activate the signup for username $username")
            throw SignupActivationException("Failed to activate the signup for username $username")
        }
        logger.info("successfully activated signup for user $username")
        return toModel(model)
    }

    fun deactivate(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)
        val model = try {
            this.signupService.deactivate(domain)
        } catch (ex: Exception) {
            logger.error("Failed to deactivate the signup for username $username")
            throw SignupActivationException("Failed to deactivate the signup for username $username")
        }
        logger.info("successfully deactivated signup for user $username")
        return toModel(model)
    }

    fun resendCode(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val result = try {
            signupService.resendCode(domain, notification(domain))
        } catch (ex: Exception) {
            logger.error("Failed to signup a new user $username")
            throw SignupRegistrationException("Failed to signup a new user $username")
        }
        logger.info("successfully resent an activation code for user $username")
        return toModel(result)
    }

    fun verifyByCode(username: String, code: String): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val model = try {
            this.signupService.verifyByCode(domain, code)
        } catch (ex: Exception) {
            logger.error("Failed to verify by code for the signup of username $username")
            throw SignupActivationException("Failed to verify by code for the signup of username $username")
        }
        logger.info("successfully verified the activation code for user $username")
        return toModel(model)
    }

    fun state(username: String): SignupStateModel {
        val domain = signupService.findByUsername(username)

        return toModel(domain.state!!)
    }

    fun verifyByEmailFromToken(token: String): SignupStateModel {
        val model = try {
            this.signupService.verifyByCodeFromToken(token)
        } catch (ex: Exception) {
            logger.error("Failed to verify by email from the token $token")
            throw SignupActivationException("Failed to verify by email from the token $token")
        }
        logger.info("successfully verified the token by email")
        return toModel(model)
    }

    fun updateStatus(username: String, status: Status): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val model = try {
            this.signupService.updateStatus(domain, status)
        } catch (ex: Exception) {
            logger.error("Failed to update the status of signup for username $username")
            throw SignupStatusUpdateException("Failed to update the status of signup for username $username")
        }
        logger.info("successfully updated the status for user $username")
        return toModel(model)
    }

    fun uploadPortrait(username: String, file: MultipartFile): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.PORTRAIT))
        metafileService.save(metaFile, file.inputStream)

        val model = try {
            this.signupService.portraitUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the portrait of username $username")
            throw SignupUploadException("Failed to upload the portrait of username $username\"")
        }
        logger.info("successfully upload the portrait for user $username")
        return toModel(model)
    }

    fun uploadResume(username: String, file: MultipartFile): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV))
        metafileService.save(metaFile, file.inputStream)

        val model = try {
            this.signupService.resumeUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the resume of username $username")
            throw SignupUploadException("Failed to upload the resume of username $username\"")
        }
        logger.info("successfully upload the resume for user $username")
        return toModel(model)
    }

    fun uploadResumeLinkedin(username: String, file: MultipartFile): SignupStateModel {
        val domain = signupService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV_LINKEDIN))
        metafileService.save(metaFile, file.inputStream)

        val model = try {
            this.signupService.resumeLinkedinUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the resume linkedin of username $username")
            throw SignupUploadException("Failed to upload the resume linkedin of username $username\"")
        }
        logger.info("successfully upload the resume linkedin for user $username")
        return toModel(model)
    }

    private fun notification(signup: SignupDomain): NotificationDomain {
        val emailActivationLink = emailValidationLink(signup)
        val smsContent = messageSource.getMessage("signup.sms.content", arrayOf(signup.firstname, signup.activationCode), locale)
        val emailSubject = messageSource.getMessage("signup.mail.subject", arrayOf(), locale)
        val emailGreeting = messageSource.getMessage("signup.mail.greeting", arrayOf(signup.firstname), locale)
        val emailContent = messageSource.getMessage("signup.mail.content", arrayOf(emailActivationLink, signup.activationCode), locale)

        return NotificationDomain.Builder(signup.username)
                .smsFrom(smsFrom)
                .smsTo(signup.phoneNumber)
                .smsContent(smsContent)
                .emailFrom(emailFrom)
                .emailTo(signup.username)
                .emailSubject(emailSubject)
                .emailGreeting(emailGreeting)
                .emailContent(emailContent)
                .build()
    }

    private fun emailValidationLink(signup: SignupDomain): String {
        return backendBaseUrl + "/signup/email/verify?token=" + signup.activationToken + "." + randomString()
    }

    fun randomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..12)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

}

class SignupUserNotFoundException(private val msg: String) : Throwable(msg) {}
class SignupRegistrationException(private val msg: String) : Throwable(msg) {}
class SignupDeleteException(private val msg: String) : Throwable(msg) {}
class SignupActivationException(private val msg: String) : Throwable(msg) {}
class SignupStatusUpdateException(private val msg: String) : Throwable(msg) {}
class SignupUploadException(private val msg: String) : Throwable(msg) {}
