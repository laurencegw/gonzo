package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.core.blog.api.BlogDraftEntryNew
import com.binarymonks.gonzo.core.blog.persistence.BlogRepo
import com.binarymonks.gonzo.core.common.ValidationException
import com.binarymonks.gonzo.core.common.ValidationMessage
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito


class BlogServiceValidationTest {


    lateinit var blogService: BlogService

    @BeforeEach
    fun setUp() {
        blogService = BlogService(
                Mockito.mock(BlogRepo::class.java),
                Mockito.mock(UserRepo::class.java)
        )
    }

    @Test
    fun testCreateBlog_titleNotEmpty() {
        val newBlogEntry = BlogDraftEntryNew(
                title = "",
                content = "A bit of content",
                authorID = 1
        )

        val exception = assertThrows<ValidationException> {
            blogService.createBlogEntry(newBlogEntry)
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