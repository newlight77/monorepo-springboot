package io.tricefal.core.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import kotlin.reflect.full.memberProperties

@ConfigurationProperties("security.oauth")
data class OAuthProperties (
    var issuer: String = "",
    var audience: String = "",
    var clientId: String = "",
    var clientSecret: String = "" ) {

    override fun toString() : String{
        return this.javaClass.name + " [" + this.javaClass.kotlin.memberProperties
                .joinToString { it -> "{\'name=${it.name}\' 'value=${it.get(this).toString()}'}" } + "]"
    }
}
