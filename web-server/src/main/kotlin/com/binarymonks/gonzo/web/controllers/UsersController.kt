package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.users.api.*
import com.binarymonks.gonzo.core.users.service.UsersAuthService
import com.binarymonks.gonzo.web.Routes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class UsersController {

    @Autowired
    lateinit var userServiceAuth: UsersAuthService

    @PostMapping("${Routes.USERS}")
    fun createUser(@RequestBody user: UserNew)=userServiceAuth.createUser(
            getCredentials(), user
    )

    fun updateUser(@RequestBody user: UserUpdate): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getUserByEmail(email: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun requestPasswordResetEmail(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun resetPassword(@RequestBody passwordReset: PasswordReset) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("${Routes.USERS}/{id}/roles")
    fun setUserRole(@PathVariable id: Long, @RequestBody userRoleUpdate: UserRoleUpdate) {
        userServiceAuth.setUserRole(getCredentials(),userRoleUpdate)
    }
}