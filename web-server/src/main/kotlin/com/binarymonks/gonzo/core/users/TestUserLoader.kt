package com.binarymonks.gonzo.core.users

import com.binarymonks.gonzo.core.users.api.UserNew
import com.binarymonks.gonzo.core.users.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class TestUserLoader {

    @Autowired
    lateinit var userService:UserService

    @EventListener
    fun onApplicationEvent(event: ApplicationStartedEvent){
        userService.createUser(UserNew(
                handle = "gonzocoder",
                email = "gonzo@coder.com",
                password = "password"
        ))
    }
}