package io.oneprofile.core.login

import io.oneprofile.core.exception.GlobalNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class LoginWebHandler(val loginService: ILoginService) {
    fun login(login: LoginModel) {
        loginService.create(fromModel(login))
    }

    fun findByUsername(username: String): List<LoginModel> {
        if (username.isEmpty()) throw throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid username $username")
        val lists = try {
            this.loginService.findByUsername(username)
        } catch (ex: Throwable) {
            throw GlobalNotFoundException("Failed to find logins with username $username", ex)
        }
        return lists.map { toModel(it) }
    }
}