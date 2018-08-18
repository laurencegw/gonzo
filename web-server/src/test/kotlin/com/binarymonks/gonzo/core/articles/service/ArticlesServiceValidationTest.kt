package com.binarymonks.gonzo.core.articles.service

import com.binarymonks.gonzo.core.articles.api.ArticleDraftNew
import com.binarymonks.gonzo.core.articles.persistence.ArticleRepo
import com.binarymonks.gonzo.core.common.ValidationException
import com.binarymonks.gonzo.core.common.ValidationMessage
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito


class ArticlesServiceValidationTest {


    lateinit var articleService: ArticlesService

    @BeforeEach
    fun setUp() {
        articleService = ArticlesService(
                Mockito.mock(ArticleRepo::class.java),
                Mockito.mock(UserRepo::class.java)
        )
    }

    @Test
    fun testCreateArticle_titleNotEmpty() {
        val newArticleEntry = ArticleDraftNew(
                title = "",
                content = "A bit of content",
                authorID = 1
        )

        val exception = assertThrows<ValidationException> {
            articleService.createArticleEntry(newArticleEntry)
        }

        Assertions.assertEquals(
                listOf(
                        ValidationMessage(
                                "title",
                                "Title is required"
                        )
                )
                , exception.validationMessages.messages
        )
    }

}