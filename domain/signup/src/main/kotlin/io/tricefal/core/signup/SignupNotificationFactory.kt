package io.tricefal.core.signup

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.util.*

open class SignupNotificationFactory() {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val resourceBundle = ResourceBundle.getBundle("i18n.signup", Locale.FRANCE)

    fun signupSmsVerificationNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): SmsNotificationDomain {
        val smsContent = getString("signup.sms.verification.content", signup.firstname, signup.activationCode)

        return SmsNotificationDomain.Builder(signup.username)
                .smsFrom(metaNotification.smsFrom)
                .smsTo(signup.phoneNumber)
                .smsContent(smsContent)
                .build()
    }

    fun singupEmailVerificationNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailActivationLink = emailValidationLink(signup, metaNotification)
        val emailSubject = getString("signup.email.verification.subject")
        val emailGreeting = getString("signup.email.verification.greeting", signup.firstname)
        val emailContent = getString("signup.email.verification.content", emailActivationLink, showActivationCodeByEnv(metaNotification.targetEnv, signup.activationCode))
        val emailSignature = getString("signup.email.verification.signature")

        return EmailNotificationDomain.Builder(signup.username)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(signup.username)
            .emailBcc(metaNotification.emailAdmin)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
            .emailSignature(emailSignature)
            .targetEnv(metaNotification.targetEnv)
            .build()
    }

    private fun showActivationCodeByEnv(env: String?, activationCode: String?) = mapOf(
        null to "",
        "" to "",
        "prod" to "",
        "dev" to activationCode,
        "ci" to activationCode,
    )[env]

    fun accountActivatedEmailNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("signup.account.activated.email.subject")
        val emailGreeting = getString("signup.account.activated.email.greeting", signup.firstname)
        val emailContent = getString("signup.account.activated.email.content")
        val emailSignature = getString("signup.account.activated.email.signature")

        return EmailNotificationDomain.Builder(signup.username)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(signup.username)
            .emailBcc(metaNotification.emailAdmin)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
            .emailSignature(emailSignature)
            .targetEnv(metaNotification.targetEnv)
            .build()
    }

    fun accountToBeActivatedEmailToAdmin(signup: SignupDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("signup.account.tobe.activated.email.subject")
        val emailGreeting = getString("signup.account.tobe.activated.email.greeting", "admin")
        val emailContent = getString("signup.account.tobe.activated.email.content", signup.username)
        val emailSignature = getString("signup.account.tobe.activated.email.signature")

        return EmailNotificationDomain.Builder(signup.username)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(metaNotification.emailAdmin)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
            .emailSignature(emailSignature)
            .targetEnv(metaNotification.targetEnv)
            .build()
    }

    fun waitingForActivationEmailNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("signup.waiting.for.activated.email.subject")
        val emailGreeting = getString("signup.waiting.for.activated.email.greeting", signup.firstname)
        val emailContent = getString("signup.waiting.for.activated.email.content")
        val emailSignature = getString("signup.waiting.for.activated.email.signature")

        return EmailNotificationDomain.Builder(signup.username)
            .emailFrom(metaNotification.emailFrom)
            .emailTo(signup.username)
            .emailBcc(metaNotification.emailAdmin)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
            .emailSignature(emailSignature)
            .targetEnv(metaNotification.targetEnv)
            .build()
    }

    private fun emailValidationLink(signup: SignupDomain, metaNotification: MetaNotificationDomain): String {
        return metaNotification.baseUrl + "/signup/email/verify?token=" + signup.activationToken + "." + randomString()
    }

    fun randomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..12)
                .map { kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
    }

    private fun getString(key: String, vararg params: String?): String? {
        return try {
            MessageFormat.format(resourceBundle.getString(key), *params)
        } catch (e: MissingResourceException) {
            throw SignupResourceBundleMissingKeyException("Failed to retrieve the value for key=$key in resource bundle i18n")
        }
    }
}