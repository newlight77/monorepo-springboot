package io.oneprofile.signup.user

interface IUserService {
    fun updatePassword(username: String, newPassword: String) : Boolean
}