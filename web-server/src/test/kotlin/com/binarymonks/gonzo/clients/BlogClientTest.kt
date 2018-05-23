package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.blogEntry
import com.binarymonks.gonzo.blogEntryHeader
import com.binarymonks.gonzo.blogEntryNew
import com.binarymonks.gonzo.blogEntryUpdate
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.web.GonzoApplication
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
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
@Ignore("Some issue with http")
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

    @Test()
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

    @Test
    fun getBlogEntryHeaders(){
        val expectedHeaders = listOf(
                blogEntryHeader().copy(id=1),
                blogEntryHeader().copy(id=2)
        )

        Mockito.`when`(blogServiceMock.getBlogEntryHeaders()).thenReturn(expectedHeaders)

        val actual = blogClient.getBlogEntryHeaders()

        Mockito.verify(blogServiceMock).getBlogEntryHeaders()
        Assert.assertEquals(expectedHeaders, actual)
    }

    @Test
    fun getBlogEntryById(){
        val expected = blogEntry()
        Mockito.`when`(blogServiceMock.getBlogEntryById(expected.id)).thenReturn(expected)

        val actual = blogClient.getBlogEntryById(expected.id)

        Mockito.verify(blogServiceMock).getBlogEntryById(expected.id)
        Assert.assertEquals(expected, actual)
    }

}