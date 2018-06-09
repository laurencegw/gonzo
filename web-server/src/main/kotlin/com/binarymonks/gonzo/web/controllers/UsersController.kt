package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.core.users.service.UsersAuthService
import com.binarymonks.gonzo.web.Routes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UsersController:Users {

    @Autowired
    lateinit var userServiceAuth: UsersAuthService

    @PostMapping("${Routes.USERS}")
    override fun createUser(@RequestBody user: UserNew)=userServiceAuth.createUser(
            getCredentials(), user
    )

    override fun updateUser(@RequestBody user: UserUpdate): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserByEmail(email: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestPasswordResetEmail(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetPassword(@RequestBody passwordReset: PasswordReset) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserRole(userID: Long, role: Role) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}