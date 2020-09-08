package io.tricefal.core.okta

import io.tricefal.core.signup.SignupDomain

interface IamRegisterService {
    fun register(signup: SignupDomain): Boolean
}