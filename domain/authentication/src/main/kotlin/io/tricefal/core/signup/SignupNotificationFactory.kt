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

    fun signupSmsNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): SmsNotificationDomain {
        val smsContent = getString("signup.sms.content", signup.firstname, signup.activationCode)

        return SmsNotificationDomain.Builder(signup.username)
                .smsFrom(metaNotification.smsFrom)
                .smsTo(signup.phoneNumber)
                .smsContent(smsContent)
                .build()
    }

    fun singupEmailNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailActivationLink = emailValidationLink(signup, metaNotification)
        val emailSubject = getString("signup.mail.subject")
        val emailGreeting = getString("signup.mail.greeting", signup.firstname)
        val emailContent = getString("signup.mail.content", emailActivationLink, signup.activationCode)

        return EmailNotificationDomain.Builder(signup.username)
                .emailFrom(metaNotification.emailFrom)
                .emailTo(signup.username)
                .emailSubject(emailSubject)
                .emailGreeting(emailGreeting)
                .emailContent(emailContent)
                .build()
    }

    fun activatedEmailNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): EmailNotificationDomain {
        val emailSubject = getString("signup.activated.mail.subject")
        val emailGreeting = getString("signup.activated.mail.greeting", signup.firstname)
        val emailContent = getString("signup.activated.mail.content")

        return EmailNotificationDomain.Builder(signup.username)
            .emailFrom(metaNotification.emailAdmin)
            .emailTo(signup.username)
            .emailSubject(emailSubject)
            .emailGreeting(emailGreeting)
            .emailContent(emailContent)
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