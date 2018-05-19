package com.binarymonks.me.clients

import com.binarymonks.me.blogEntry
import com.binarymonks.me.blogEntryNew
import com.binarymonks.me.blogEntryUpdate
import com.binarymonks.me.core.blog.service.BlogService
import com.binarymonks.me.web.GonzoApplication
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class]
)
class BlogClientTest {

    @LocalServerPort
    var port: Int = -1

    @MockBean
    lateinit var blogServiceMock: BlogService

    lateinit var blogClient: BlogClient

    @Before
    fun setUp() {
        blogClient = BlogClient("http://localhost:$port")
    }

    @Test
    fun createBlogEntry() {
        val newBlogEntry = blogEntryNew()
        val expectedBlogEntry = blogEntry()
        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(expectedBlogEntry)

        val actual = blogClient.createBlogEntry(newBlogEntry)

        Mockito.verify(blogServiceMock).createBlogEntry(newBlogEntry)
        Assert.assertEquals(expectedBlogEntry, actual)
    }

    @Test
    fun updateBlogEntry() {
        val blogEntryUpdate = blogEntryUpdate()
        val expectedBlogEntry = blogEntry()
        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(expectedBlogEntry)

        val actual = blogClient.updateBlogEntry(blogEntryUpdate)

        Mockito.verify(blogServiceMock).updateBlogEntry(blogEntryUpdate)
        Assert.assertEquals(expectedBlogEntry, actual)
    }


}