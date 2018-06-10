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
    fun createUser(@RequestBody user: UserNew) = userServiceAuth.createUser(
            getCredentials(), user
    )

    @PutMapping("${Routes.USERS}/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserUpdate): User {
        return userServiceAuth.updateUser(getCredentials(), user.copy(id = id))
    }


    fun requestPasswordResetEmail(email: String) {
        userServiceAuth.requestPasswordResetEmail(getCredentials(), email)
    }

    fun resetPassword(@RequestBody passwordReset: PasswordReset) {
        userServiceAuth.resetPassword(getCredentials(), passwordReset)
    }

    @PutMapping("${Routes.USERS}/{id}/roles")
    fun setUserRole(@PathVariable id: Long, @RequestBody userRoleUpdate: UserRoleUpdate) {
        userServiceAuth.setUserRole(getCredentials(), userRoleUpdate.copy(id = id))
    }
}