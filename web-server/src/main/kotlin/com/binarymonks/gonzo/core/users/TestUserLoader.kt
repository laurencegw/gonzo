package com.binarymonks.gonzo.core.users

import com.binarymonks.gonzo.core.blog.api.BlogDraftEntryNew
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.UserNew
import com.binarymonks.gonzo.core.users.api.UserRoleUpdate
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
        userService.setUserRole(UserRoleUpdate(user.id, Role.ADMIN))

        blogService.createBlogEntry(BlogDraftEntryNew(
                "Never published",
                "This blog is only in draft state - no published content",
                user.id
        ))

        var blog = blogService.createBlogEntry(BlogDraftEntryNew(
                "Published, No Changes",
                "This blog is published and no current changes",
                user.id
        ))
        blogService.publishBlogEntry(blog.id)

        blog = blogService.createBlogEntry(BlogDraftEntryNew(
                "Published, Unpublished Changes",
                "This is content that has been published",
                user.id
        ))
        blogService.publishBlogEntry(blog.id)
        blogService.updateBlogEntry(blog.toUpdate().copy(
                content = "This is content that has not been published"
        ))

//        for (i in (0..5)){
//            blogService.createBlogEntry(BlogDraftEntryNew(
//                    "My Blog $i",
//                    "This is the content for blog number $i",
//                    user.id
//            ))
//        }

    }
}