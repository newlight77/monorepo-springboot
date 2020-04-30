package io.tricefal.core.login.model

import io.tricefal.core.login.domain.LoginDomain
import java.time.Instant

data class LoginModel(var id: Long,
                        val username: String,
                        val lastLogin: Instant,
                        val ipAddress: String,
                        val success: Boolean)

fun fromDomain(domain: LoginDomain): LoginModel {
    return LoginModel(
            domain.id,
            domain.username,
            domain.lastLogin,
            domain.ipAddress,
            domain.success
    )
}

fun toDomain(model: LoginModel): LoginDomain {
    return LoginDomain(
            model.id,
            model.username,
            model.lastLogin,
            model.ipAddress,
            model.success
    )
}