package com.binarymonks.gonzo.core.users

import com.binarymonks.gonzo.core.articles.api.ArticleDraftNew
import com.binarymonks.gonzo.core.articles.service.ArticlesService
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
    lateinit var articleService:ArticlesService

    @EventListener
    fun onApplicationEvent(event: ApplicationStartedEvent){
        val user = userService.createUser(UserNew(
                handle = "gonzocoder",
                email = "gonzo@coder.com",
                password = "password"
        ))
        userService.setUserRole(UserRoleUpdate(user.id, Role.ADMIN))

        articleService.createArticleEntry(ArticleDraftNew(
                "Never published",
                "## Markdown is supported\n" +
                        "This **articles** is only in draft state - no published content\n" +
                        "\n" +
                        "Still working on this draft\n" +
                        "\n" +
                        "![vegeta](https://vignette.wikia.nocookie.net/dbz-dokkanbattle/images/9/95/Artwork_1012640.png/revision/latest/scale-to-width-down/250?cb=20180325041540)\n" +
                        "\n" +
                        "<h2>And so are html tags </h2>",
                user.id
        ))

        var article = articleService.createArticleEntry(ArticleDraftNew(
                "Published, No Changes",
                "This articles is published and no current changes",
                user.id
        ))
        articleService.publishArticleEntry(article.id)

        article = articleService.createArticleEntry(ArticleDraftNew(
                "Published, Unpublished Changes",
                "This is content that has been published",
                user.id
        ))
        articleService.publishArticleEntry(article.id)
        articleService.updateArticleEntry(article.toUpdate().copy(
                content = "This is content that has not been published"
        ))

//        for (i in (0..5)){
//            articleService.createArticleEntry(ArticleDraftNew(
//                    "My Articles $i",
//                    "This is the content for articles number $i",
//                    user.id
//            ))
//        }

    }
}