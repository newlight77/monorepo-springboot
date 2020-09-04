package io.tricefal.core.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource
import kotlin.reflect.full.memberProperties

@ConfigurationProperties("security.jwt")
class JwtProperties {
    lateinit var keyStore: Resource
    lateinit var keyStorePassword: String
    lateinit var keyPairAlias: String
    lateinit var keyPairPassword: String

    override fun toString() : String{
        return this.javaClass.name + " [" + this.javaClass.kotlin.memberProperties
                .joinToString { it -> "\'name=${it.name}\' 'value=${it.get(this).toString()}'" } + "]"

    }

//    override fun toString() : String{
//        var ret : String = ""
//        for (memberProperty in this.javaClass.kotlin.memberProperties) {
//            ret += ("Property:${memberProperty.name} value:${memberProperty.get(this).toString()}\n")
//        }
//        return ret
//    }
}