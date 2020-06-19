package io.tricefal.core.signup

import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.email.EmailTemplate
import io.tricefal.core.metafile.MetafileModel
import io.tricefal.core.metafile.MetafileRepository
import io.tricefal.core.okta.OktaService
import io.tricefal.core.twilio.SmsMessage
import io.tricefal.core.twilio.SmsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.security.SecureRandom
import java.time.Instant
import java.util.*


@Service
@PropertySource("classpath:application.yml", "classpath:twilio.yml", "classpath:okta.yml")
class SignupHandler(val signupService: ISignupService,
                    val metafileRepository: MetafileRepository,
                    val oktaService: OktaService,
                    val mailService: EmailService,
                    val smsService: SmsService,
                    private final val env: Environment,
                    private final val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!

    val locale: Locale = LocaleContextHolder.getLocale()

    fun signup(signup: SignupModel): SignupResult {

        //oktaService.register(fromModel(signup))
        signup.activationCode = generateCode()
        sendEmail(signup)
        val signupDomain = signupService.signup(fromModel(signup))
        sendSms(signup)

        return SignupResult.Builder(signupDomain.username)
                .signup(toModel(signupDomain))
                .created(true)
                .emailSent(true)
                .activationCodeSent(true)
                .build()
    }

    fun findByUsername(username: String): Optional<SignupModel> {
        return signupService.findByUsername(username).map { toModel(it) }
    }

    fun activate(username: String, code: String): SignupResult {
        // activation de compte
//        val signup = this.signupService.findByUsername(username)
//        signup.ifPresent { s: SignupDomain -> if (s.activationCode.equals(code)) signupService.activate()}
        return SignupResult.Builder(username).build()
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

    private fun sendSms(signup: SignupModel) {
        val smsContent = messageSource.getMessage("signup.sms.content", arrayOf(signup.firstname, signup.activationCode), locale)
        val message = SmsMessage.Builder()
                .from(smsFrom)
                .to(signup.phoneNumber!!)
                .content(smsContent)
                .build()
        smsService.send(message)
    }

    private fun sendEmail(signup: SignupModel) {
        logger.info("Sending an email")
        val mailSubject = messageSource.getMessage("signup.mail.subject", arrayOf(), locale)
        val mailGreeting = messageSource.getMessage("signup.mail.greeting", arrayOf(signup.firstname), locale)
        val mailContent = messageSource.getMessage("signup.mail.content", arrayOf(), locale)
        val message = EmailMessage.Builder()
                .from(emailFrom)
                .to(signup.username)
                .subject(mailSubject)
                .content(mailContent)
                .emailTemplate(EmailTemplate.SIGNUP)
                .model(hashMapOf("greeting" to mailGreeting, "content" to mailContent))
                .build()
        mailService.send(message)
        logger.info("An Email has been sent")
    }

    fun generateCode(): String {
        return SecureRandom().nextGaussian().toString().takeLast(6)
    }

}