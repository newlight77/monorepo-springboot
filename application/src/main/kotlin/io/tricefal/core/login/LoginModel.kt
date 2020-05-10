package io.tricefal.core.login

import java.time.Instant

data class LoginModel(val username: String,
                      val loginDate: Instant,
                      val ipAddress: String,
                      val device: String,
                      val success: Boolean)

fun toModel(domain: LoginDomain): LoginModel {
    return LoginModel(
            domain.username,
            domain.loginDate,
            domain.ipAddress,
            domain.device,
            domain.success
    )
}

fun fromModel(model: LoginModel): LoginDomain {
    return LoginDomain(
            model.username,
            model.loginDate,
            model.ipAddress,
            model.device,
            model.success
    )
}