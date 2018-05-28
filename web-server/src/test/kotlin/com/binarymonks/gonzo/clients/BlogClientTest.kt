package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.*
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.common.NotAuthentic
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.core.users.service.UserService
import com.binarymonks.gonzo.web.GonzoApplication
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class]
)
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
class BlogClientTest {

    @LocalServerPort
    var port: Int = -1

    @MockBean
    lateinit var blogServiceMock: BlogService

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userRepo: UserRepo

    lateinit var blogClient: BlogClient

    @Before
    fun setUp() {
        blogClient = BlogClient("http://localhost:$port")
        userRepo.deleteAll()
    }

    @Test(expected = NotAuthentic::class)
    fun createBlogEntry_notSignedIn() {
        val newBlogEntry = blogEntryNew()
        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(blogEntry())
        blogClient.createBlogEntry(newBlogEntry)
    }

    @Test()
    fun createBlogEntry() {
        login()

        val newBlogEntry = blogEntryNew()
        val expectedBlogEntry = blogEntry()
        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(expectedBlogEntry)

        val actual = blogClient.createBlogEntry(newBlogEntry)

        Mockito.verify(blogServiceMock).createBlogEntry(newBlogEntry)
        Assert.assertEquals(expectedBlogEntry, actual)
    }

    private fun login() {
        val newUser = userNew()
        userService.createUser(newUser)
        blogClient.signIn(newUser.email, newUser.password)
    }

    @Test
    @Ignore("Need an api to sign in with first")
    fun updateBlogEntry() {
        val blogEntryUpdate = blogEntryUpdate()
        val expectedBlogEntry = blogEntry()
        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(expectedBlogEntry)

        val actual = blogClient.updateBlogEntry(blogEntryUpdate)

        Mockito.verify(blogServiceMock).updateBlogEntry(blogEntryUpdate)
        Assert.assertEquals(expectedBlogEntry, actual)
    }

    @Test(expected = NotAuthentic::class)
    fun updateBlogEntry_notSignedIn() {
        val blogEntryUpdate = blogEntryUpdate()
        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(blogEntry())

        blogClient.updateBlogEntry(blogEntryUpdate)
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