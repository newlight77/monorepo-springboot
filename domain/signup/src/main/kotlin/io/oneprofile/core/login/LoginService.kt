package io.oneprofile.core.login

class LoginService(private var repository: ILoginRepository) : ILoginService {
    override fun create(login: LoginDomain) {
        repository.save(login)
    }

    override fun findByUsername(username: String): List<LoginDomain> {
        return repository.findByUsername(username)
    }
}