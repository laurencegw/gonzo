package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.blogEntryNew
import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.blog.api.BlogEntryHeader
import com.binarymonks.gonzo.core.blog.api.BlogEntryNew
import com.binarymonks.gonzo.core.blog.api.BlogEntryUpdate
import com.binarymonks.gonzo.core.test.GonzoTestConfig
import com.binarymonks.gonzo.core.test.harness.TestDataManager
import com.binarymonks.gonzo.core.time.clock
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.service.UserService
import com.binarymonks.gonzo.newUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@ExtendWith(SpringExtension::class)
@ContextConfiguration(
        classes = [
            GonzoTestConfig::class
        ],
        loader = AnnotationConfigContextLoader::class
)

class BlogServiceTest {

    lateinit var mockClock: Clock

    @Autowired
    lateinit var blogService: BlogService
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var testDataManager: TestDataManager

    lateinit var user: User

    @BeforeEach
    fun setUp() {
        testDataManager.clearData()
        mockClock = Mockito.mock(Clock::class.java)
        clock = mockClock
        user = userService.createUser(newUser())
        itIsNow()
    }

    @Test
    fun createAndGetBlogEntry() {
        val now = itIsNow()

        val newBlogEntry = BlogEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )


        val created = blogService.createBlogEntry(newBlogEntry)

        val expectedBlogEntry = BlogEntry(
                id = created.id,
                title = newBlogEntry.title,
                content = newBlogEntry.content,
                author = user.toPublicHeader(),
                published = false,
                publishedOn = now,
                updated = now,
                created = now
        )

        Assertions.assertEquals(expectedBlogEntry, created)

        val retrieved = blogService.getBlogEntryById(created.id)

        Assertions.assertEquals(expectedBlogEntry, retrieved)

    }

    @Test
    fun getById_DoesNotExist() {
        Assertions.assertThrows(NoSuchElementException::class.java, {
            blogService.getBlogEntryById(-1)
        })
    }

    @Test
    fun publishBlogEntry() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val created = blogService.createBlogEntry(newBlogEntry)

        val publishedTime = itIsNow(createdTime.plusDays(1))

        blogService.publishBlogEntry(created.id)

        val expected = created.copy(publishedOn = publishedTime)

        val actual = blogService.getBlogEntryById(created.id)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun update_PublishedStaysPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val created = blogService.createBlogEntry(newBlogEntry)

        val updatedTime = itIsNow(createdTime.plusDays(1))

        val update = created.copy(title = "Changed Title", content = "New Content")

        val expected = update.copy(updated = updatedTime)

        val updated = blogService.updateBlogEntry(update.toUpdate())

        Assertions.assertEquals(expected, updated)
        Assertions.assertEquals(expected, blogService.getBlogEntryById(created.id))
    }

    @Test
    fun updatePublished_keepLastPublishedDate() {
        val createdTime = itIsNow()

        val newBlogEntry = BlogEntryNew(
                title = "Some Blog Entry",
                content = "A bit of content",
                authorID = user.id
        )

        val created = blogService.createBlogEntry(newBlogEntry)
        blogService.publishBlogEntry(created.id)

        val updatedTime = itIsNow(createdTime.plusDays(1))

        val update = created.toUpdate().copy(
                title = "Changed Title",
                content = "New Content"
        )

        val expected = created.copy(updated = updatedTime)

        val updated = blogService.updateBlogEntry(update)

        Assertions.assertEquals(expected, updated)
        Assertions.assertEquals(expected, blogService.getBlogEntryById(created.id))
    }

    @Test
    fun update_DoesNotExist() {
        Assertions.assertThrows(NoSuchElementException::class.java, {
            blogService.updateBlogEntry(BlogEntryUpdate(-1, "", ""))
        })
    }

    @Test
    fun getBlogEntryHeaders() {
        val created: List<BlogEntryHeader> = listOf(
                blogService.createBlogEntry(blogEntryNew().copy(
                        title = "Entry1",
                        authorID = user.id
                )).toHeader(),
                blogService.createBlogEntry(blogEntryNew().copy(
                        title = "Entry2",
                        authorID = user.id
                )).toHeader()
        )
        val actual = blogService.getBlogEntryHeaders()
        for (header in created) {
            Assertions.assertTrue(actual.contains(header))
        }
    }

    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC"))): ZonedDateTime {
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }

}