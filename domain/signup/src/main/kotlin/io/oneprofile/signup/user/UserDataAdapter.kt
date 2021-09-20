package io.oneprofile.signup.user

interface UserDataAdapter {
    fun updatePassword(username: String, newPassword: String): Boolean
}