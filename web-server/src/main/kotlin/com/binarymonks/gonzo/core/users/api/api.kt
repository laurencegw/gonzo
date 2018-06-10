package com.binarymonks.gonzo.core.users.api

import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.Resource
import com.binarymonks.gonzo.core.common.Types
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime


interface Users {
    fun createUser(user: UserNew): User
    /**
     * Intended for users to update their own data.
     */
    fun updateUser(user: UserUpdate): User
    fun getUserByEmail(email: String): User
    fun requestPasswordResetEmail(email: String)
    fun resetPassword(passwordReset: PasswordReset)
    fun setUserRole(userRoleUpdate: UserRoleUpdate)
}

interface UsersAuth{
    fun createUser(credentials: Credentials, user: UserNew): User
    fun updateUser(credentials: Credentials, user: UserUpdate): User
    fun getUserByEmail(credentials: Credentials, email: String): User
    fun requestPasswordResetEmail(credentials: Credentials, email: String)
    fun resetPassword(credentials: Credentials, passwordReset: PasswordReset)
    fun setUserRole(credentials: Credentials, userRoleUpdate: UserRoleUpdate)
}

interface SignIn {
    fun login(credentials: LoginCredentials): String
    fun assertLoggedIn(token:String)
    fun getUserFromToken(token:String): User
}

open class UserResource:Resource(Types.USER)

data class UserNew @JsonCreator constructor(
        val email: String,
        val handle: String,
        val password: String
):UserResource()

data class User @JsonCreator constructor(
        val id: Long,
        val email: String,
        val handle: String,
        val role: Role,
        val firstName: String? = null,
        val lastName: String? = null
):UserResource() {
    fun toUpdate() = UserUpdate(
            id = id,
            email = email,
            firstName = firstName,
            lastName = lastName
    )

    fun toPublicHeader() = UserPublicHeader(
            id = id,
            handle = handle,
            firstName = firstName,
            lastName = lastName
    )
}

/**
 * Public information for a user
 */
data class UserPublicHeader @JsonCreator constructor(
        val id: Long,
        val handle: String,
        val firstName: String? = null,
        val lastName: String? = null
):UserResource()

data class UserUpdate @JsonCreator constructor(
        val id: Long,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null
)

data class UserRoleUpdate @JsonCreator constructor(
        val id: Long,
        val role: Role
): Resource(type=Types.USER_ROLES)

enum class Role{
    AUTHOR,
    ADMIN,
    READER
}

data class LoginCredentials @JsonCreator constructor(
        val email: String,
        val password: String
)

data class ResetToken @JsonCreator constructor(
        val expiry: ZonedDateTime,
        val token: String
)

data class PasswordReset @JsonCreator constructor(
        val userID: Long,
        val token: String,
        val newPassword: String
)

