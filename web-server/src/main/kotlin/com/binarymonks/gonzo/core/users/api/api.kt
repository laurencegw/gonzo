package com.binarymonks.gonzo.core.users.api

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime


interface Users {
    fun createUser(user: UserNew): User
    fun updateUser(user: UserUpdate): User
    fun getUserByEmail(email: String): User
    fun updatePassword(passwordUpdate: PasswordUpdate)
    fun requestPasswordResetToken(userID:Long): Token
    fun resetPassword(passwordReset: PasswordReset)
}

interface UserSignIn{
    fun login(credentials: LoginCredentials): Token
    fun signOutToken(token: String)
    fun signOutEmail(email: String)
    fun getUserByToken(token: String): User
}

data class UserNew @JsonCreator constructor(
        val email: String,
        val password: String
)

data class User @JsonCreator constructor(
        val id: Long,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null
) {
    fun toUpdate(): UserUpdate = UserUpdate(
            id=id,
            email=email,
            firstName = firstName,
            lastName = lastName
    )
}

data class UserUpdate @JsonCreator constructor(
        val id: Long,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null
)

data class LoginCredentials @JsonCreator constructor(
        val email: String,
        val password: String
)

data class Token @JsonCreator constructor(
        val contents: String,
        val expiry: ZonedDateTime,
        val token: String
)

data class PasswordUpdate @JsonCreator constructor(
        val id: Long,
        val newPassword: String
)

data class PasswordReset @JsonCreator constructor(
        val userID: Long,
        val token: String,
        val newPassword: String
)

