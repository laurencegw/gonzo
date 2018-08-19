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
    lateinit var userService: UserService

    @Autowired
    lateinit var articleService: ArticlesService

    @EventListener
    fun onApplicationEvent(event: ApplicationStartedEvent) {
        val user = userService.createUser(UserNew(
                handle = "gonzocoder",
                email = "gonzo@coder.com",
                password = "password"
        ))
        userService.setUserRole(UserRoleUpdate(user.id, Role.ADMIN))

        articleService.createArticle(ArticleDraftNew(
                "Never published",
                "This content has never seen the light of day",
                user.id
        ))

        var article = articleService.createArticle(ArticleDraftNew(
                "Published, No Changes",
                "## Markdown is supported\n" +
                        "This **article** was published, and is making use of mark down. So far it looks pretty good.\n" +
                        "\n" +
                        "\n" +
                        "![vegeta](https://vignette.wikia.nocookie.net/dbz-dokkanbattle/images/9/95/Artwork_1012640.png/revision/latest/scale-to-width-down/250?cb=20180325041540 \"Vegeta!\")\n" +
                        "\n" +
                        "<h2>And so are html tags </h2>" +
                        "\n" +
                        "The heading above was written with an html tag. So we can mix and match the html and markdown. Crazy\n",
                user.id
        ))
        articleService.publishArticle(article.id)

        article = articleService.createArticle(ArticleDraftNew(
                "Published, Unpublished Changes",
                "This is the original content, and there is some unpublished content as well",
                user.id
        ))
        articleService.publishArticle(article.id)
        articleService.updateArticle(article.toUpdate().copy(
                content = "This is content that has not been published"
        ))

//        for (i in (0..5)){
//            articleService.createArticle(ArticleDraftNew(
//                    "My Articles $i",
//                    "This is the content for articles number $i",
//                    user.id
//            ))
//        }

    }
}