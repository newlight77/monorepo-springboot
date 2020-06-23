package io.tricefal.core.signup

import io.tricefal.core.metafile.*
import io.tricefal.core.signup.fromModel
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*
import java.util.function.Consumer


@Service
@PropertySource("classpath:application.yml", "classpath:twilio.yml", "classpath:okta.yml")
class SignupHandler(val signupService: ISignupService,
                    val metafileRepository: MetafileRepository,
                    private final val env: Environment,
                    private final val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    val locale: Locale = LocaleContextHolder.getLocale()

    fun signup(signup: SignupModel): SignupStateModel {
        val notification = notificationModel(signup)
        val result = signupService.signup(fromModel(signup), fromModel(notification))
        return toModel(result)
    }

    fun findByUsername(username: String): Optional<SignupModel> {
        return signupService.findByUsername(username).map { signupDomain -> toModel(signupDomain) }
    }

    fun activate(username: String, code: String): SignupStateModel {
        val signup = this.signupService.findByUsername(username)
                .orElseThrow { IllegalStateException() }

        return toModel(this.signupService.activate(signup, code))
    }

    fun updateStatus(username: String, status: String): SignupStateModel {
        val signup = this.signupService.findByUsername(username)
                .orElseThrow { IllegalStateException() }

        return toModel(signupService.updateStatus(signup, Status.valueOf(status)))
    }

    fun uploadResume(username: String, file: MultipartFile): SignupStateModel {
        val signup = this.signupService.findByUsername(username)
                .orElseThrow { IllegalStateException() }

        val resumeMetaFile = fromModel(toMetafile(username, file))
        metafileRepository.save(resumeMetaFile, file.inputStream)

        return toModel(signupService.resumeUploaded(signup, resumeMetaFile))
    }

    private fun notificationModel(signup: SignupModel): SignupNotificationModel {
        val smsContent = messageSource.getMessage("signup.sms.content", arrayOf(signup.firstname, signup.activationCode), locale)
        val emailSubject = messageSource.getMessage("signup.mail.subject", arrayOf(), locale)
        val emailGreeting = messageSource.getMessage("signup.mail.greeting", arrayOf(signup.firstname), locale)
        val emailContent = messageSource.getMessage("signup.mail.content", arrayOf(), locale)

        return SignupNotificationModel.Builder(signup.username)
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

}