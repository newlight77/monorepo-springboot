package io.tricefal.core.login

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class LoginDataInitializer(val loginService: ILoginService) : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        modelData().map {fromModel(it)}
                .forEach() {
                    loginService.create(it)
                }
    }
}

fun modelData(): List<LoginModel> {
    return listOf(
            LoginModel(username = "kong@tricefal.com", ipAddress = "192.169.0.1", city = "paris", region = "Ile de France", device = "brave", loginDate = Instant.now(), success = false)
    )
}