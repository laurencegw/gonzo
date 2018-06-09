package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.users.api.LoginCredentials
import com.binarymonks.gonzo.core.users.service.SignInService
import com.binarymonks.gonzo.web.Routes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class SignInController {
    @Autowired
    lateinit var signInService: SignInService

    @PostMapping("/${Routes.LOGIN}")
    fun login(@RequestBody loginCredentials: LoginCredentials): String {
        return signInService.login(loginCredentials)
    }

}