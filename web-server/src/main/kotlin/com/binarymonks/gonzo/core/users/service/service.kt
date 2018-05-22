package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.users.api.*

class UserService: Users {
    override fun createUser(user: UserNew): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUser(user: UserUpdate): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserByEmail(email: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUserPassword(passwordUpdate: UserPasswordUpdate) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(credentials: LoginCredentials): LoggedInToken {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserByToken(token: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}