package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileRepository
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.SecureRandom
import java.util.*


@Service
@PropertySource("classpath:application.yml", "classpath:twilio.yml", "classpath:okta.yml")
class SignupWebHandler(val signupService: ISignupService,
                       val metafileRepository: MetafileRepository,
                       private final val env: Environment,
                       private final val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    val locale: Locale = LocaleContextHolder.getLocale()

    fun signup(signup: SignupModel): SignupStateModel {
        val activationCode = generateCode()
        val domain = fromModel(signup)
        domain.activationCode = activationCode
        domain.activationToken = encode(activationCode) + "." + encode(signup.username) + "." + randomString()
        val result = signupService.signup(domain, notification(domain))
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

    fun activateByToken(token: String): SignupStateModel {
        val values = token.split(".")
        if (values.size < 3) throw java.lang.IllegalStateException("verify email by token : the token is invalid")
        val activationCode = decode(values[0])
        val username = decode(values[1])

        val signup = this.signupService.findByUsername(username)
                .orElseThrow { IllegalStateException() }

        return toModel(this.signupService.activate(signup, activationCode))
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

    private fun notification(signup: SignupDomain): SignupNotificationDomain {
        val emailActivationLink = emailValidationLink(signup)
        val smsContent = messageSource.getMessage("signup.sms.content", arrayOf(signup.firstname, signup.activationCode), locale)
        val emailSubject = messageSource.getMessage("signup.mail.subject", arrayOf(), locale)
        val emailGreeting = messageSource.getMessage("signup.mail.greeting", arrayOf(signup.firstname), locale)
        val emailContent = messageSource.getMessage("signup.mail.content", arrayOf(emailActivationLink), locale)

        return SignupNotificationDomain.Builder(signup.username)
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
        return backendBaseUrl + "/signup/verify/email?toeken" + signup.activationToken + "." + randomString()
    }

    fun generateCode(): String {
        return SecureRandom().nextGaussian().toString().takeLast(6)
    }

    fun encode(code: String): String = Base64.getEncoder().encodeToString(code.toByteArray())

    fun decode(code: String): String = String(Base64.getDecoder().decode(code.toByteArray()))

    fun randomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..12)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

}