package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod


class UserClient(baseURL: String) : Users, AuthClient(baseURL) {
    override fun createUser(user: UserNew): User {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.USERS}",
                HttpMethod.POST,
                HttpEntity(user, createHeaders()),
                object : ParameterizedTypeReference<User>() {}
        )
        return checkNotNull(response.body)
    }

    override fun updateUser(user: UserUpdate): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserByEmail(email: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestPasswordResetEmail(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetPassword(passwordReset: PasswordReset) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserRole(userRoleUpdate: UserRoleUpdate) {
        restTemplate.put(
                "$baseURL/${Routes.userRoles(userRoleUpdate.id)}",
                HttpEntity(userRoleUpdate, createHeaders())
        )
    }
}