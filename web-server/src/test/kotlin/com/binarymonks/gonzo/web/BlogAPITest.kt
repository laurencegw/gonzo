package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.*
import com.binarymonks.gonzo.clients.BlogClient
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.common.NotAuthentic
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.core.users.service.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [GonzoApplication::class]
)
@ExtendWith(SpringExtension::class)
class BlogAPITest {

    @LocalServerPort
    var port: Int = -1

    @MockBean
    lateinit var blogServiceMock: BlogService

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userRepo: UserRepo

    lateinit var blogClient: BlogClient

    @BeforeEach
    fun setUp() {
        blogClient = BlogClient("http://localhost:$port")
        userRepo.deleteAll()
    }

    @Test()
    @Disabled("Not authorizing yet")
    fun createBlogEntry_notSignedIn() {
        val newBlogEntry = blogEntryNew()
        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(blogEntry())
        Assertions.assertThrows(NotAuthentic::class.java, { blogClient.createBlogEntry(newBlogEntry) })

    }

    @Test()
    fun createBlogEntry() {
        login()

        val newBlogEntry = blogEntryNew()
        val expectedBlogEntry = blogEntry()
        Mockito.`when`(blogServiceMock.createBlogEntry(newBlogEntry)).thenReturn(expectedBlogEntry)

        val actual = blogClient.createBlogEntry(newBlogEntry)

        Mockito.verify(blogServiceMock).createBlogEntry(newBlogEntry)
        Assertions.assertEquals(expectedBlogEntry, actual)
    }

    private fun login() {
        val newUser = newUser()
        userService.createUser(newUser)
        blogClient.signIn(newUser.email, newUser.password)
    }

    @Test
    fun updateBlogEntry() {
        login()

        val blogEntryUpdate = blogEntryUpdate()
        val expectedBlogEntry = blogEntry()
        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(expectedBlogEntry)

        val actual = blogClient.updateBlogEntry(blogEntryUpdate)

        Mockito.verify(blogServiceMock).updateBlogEntry(blogEntryUpdate)
        Assertions.assertEquals(expectedBlogEntry, actual)
    }

    @Test()
    @Disabled("Does not go through authentication yet")
    fun updateBlogEntry_notSignedIn() {
        val blogEntryUpdate = blogEntryUpdate()
        Mockito.`when`(blogServiceMock.updateBlogEntry(blogEntryUpdate)).thenReturn(blogEntry())

        Assertions.assertThrows(NotAuthentic::class.java, {
            blogClient.updateBlogEntry(blogEntryUpdate)
        })
    }

    @Test
    fun getBlogEntryHeaders() {
        val expectedHeaders = listOf(
                blogEntryHeader().copy(id = 1),
                blogEntryHeader().copy(id = 2)
        )

        Mockito.`when`(blogServiceMock.getBlogEntryHeaders()).thenReturn(expectedHeaders)

        val actual = blogClient.getBlogEntryHeaders()

        Mockito.verify(blogServiceMock).getBlogEntryHeaders()
        Assertions.assertEquals(expectedHeaders, actual)
    }

    @Test
    fun getBlogEntryById() {
        val expected = blogEntry()
        Mockito.`when`(blogServiceMock.getBlogEntryById(expected.id)).thenReturn(expected)

        val actual = blogClient.getBlogEntryById(expected.id)

        Mockito.verify(blogServiceMock).getBlogEntryById(expected.id)
        Assertions.assertEquals(expected, actual)
    }

}