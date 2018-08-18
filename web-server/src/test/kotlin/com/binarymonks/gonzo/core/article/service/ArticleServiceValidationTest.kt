package com.binarymonks.gonzo.core.article.service

import com.binarymonks.gonzo.core.article.api.ArticleDraftEntryNew
import com.binarymonks.gonzo.core.article.persistence.ArticleRepo
import com.binarymonks.gonzo.core.common.ValidationException
import com.binarymonks.gonzo.core.common.ValidationMessage
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito


class ArticleServiceValidationTest {


    lateinit var articleService: ArticleService

    @BeforeEach
    fun setUp() {
        articleService = ArticleService(
                Mockito.mock(ArticleRepo::class.java),
                Mockito.mock(UserRepo::class.java)
        )
    }

    @Test
    fun testCreateArticle_titleNotEmpty() {
        val newArticleEntry = ArticleDraftEntryNew(
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