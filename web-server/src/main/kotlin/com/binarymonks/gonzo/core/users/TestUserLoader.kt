package com.binarymonks.gonzo.core.users

import com.binarymonks.gonzo.core.blog.api.BlogDraftEntryNew
import com.binarymonks.gonzo.core.blog.service.BlogService
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

    @Autowired
    lateinit var blogService:BlogService

    @EventListener
    fun onApplicationEvent(event: ApplicationStartedEvent){
        val user = userService.createUser(UserNew(
                handle = "gonzocoder",
                email = "gonzo@coder.com",
                password = "password"
        ))
        for (i in (0..20)){
            blogService.createBlogEntry(BlogDraftEntryNew(
                    "My Blog $i",
                    "This is the content for blog number $i",
                    user.id
            ))
        }

    }
}