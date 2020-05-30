package io.tricefal.core.signup

class SignupResult
    private constructor(
        val username: String,
        val signup: SignupModel?,
        val created: Boolean?,
        val emailSent: Boolean?,
        val activationCodeSent: Boolean?,
        val activated: Boolean?,
        val validated: Boolean?) {

    data class Builder (
            val username: String,
            var signup: SignupModel? = null,
            var created: Boolean? = null,
            var emailSent: Boolean? = null,
            var activationCodeSent: Boolean? = null,
            var activated: Boolean? = null,
            var validated: Boolean? = null) {
        fun signup(signup: SignupModel?) = apply { this.signup = signup }
        fun created(created: Boolean?) = apply { this.created = created }
        fun emailSent(emailSent: Boolean?) = apply { this.emailSent = emailSent }
        fun activationCodeSent(activationCodeSent: Boolean?) = apply { this.activationCodeSent = activationCodeSent }
        fun activated(activated: Boolean?) = apply { this.activated = activated }
        fun validated(validated: Boolean?) = apply { this.validated = validated }
        fun build() = SignupResult(username, signup, created, emailSent, activationCodeSent, activated, validated)
    }
}
