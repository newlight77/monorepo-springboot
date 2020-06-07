package io.tricefal.core.signup

import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.email.EmailTemplate
import io.tricefal.core.metafile.MetafileModel
import io.tricefal.core.metafile.MetafileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap


@Service
class SignupHandler(val signupService: ISignupService,
                    val metafileRepository: MetafileRepository,
                    val mailService: EmailService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${data.files.path}")
    lateinit var filesPath: String

    @Value("\${mail.from}")
    lateinit var from: String

    @Autowired
    lateinit var messageSource: MessageSource

    val locale: Locale = LocaleContextHolder.getLocale()

    fun signup(signup: SignupModel): SignupResult {

        // TODO create account on okta

        sendEmail(signup)

        val signupDomain = signupService.signup(fromModel(signup))

        // TODO send activation code via sms to phone number
        // create a SMS notification infra adapter

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

    private fun sendEmail(signup: SignupModel) {
        logger.info("Sending an email")
        val mailSubject = messageSource.getMessage("signup.mail.subject", arrayOf(), locale)
        val mailContent = messageSource.getMessage("signup.mail.content", arrayOf(), locale)
        val message = EmailMessage.Builder()
                .from(from)
                .to(signup.username)
                .subject(mailSubject)
                .content(mailContent)
                .emailTemplate(EmailTemplate.SIGNUP)
                .model(hashMapOf("content" to mailContent))
                .build()
        mailService.send(message)
        logger.info("An Email has been sent")
    }
}