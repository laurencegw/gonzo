package com.binarymonks.me.core.blog.service

import com.binarymonks.me.TestConfig
import com.binarymonks.me.core.blog.api.BlogEntry
import com.binarymonks.me.core.blog.api.BlogEntryHeader
import com.binarymonks.me.core.blog.api.NewBlogEntry
import com.binarymonks.me.core.blog.api.UpdateBlogEntry
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(TestConfig::class), loader = AnnotationConfigContextLoader::class)
class BlogServiceTest {

    lateinit var mockClock: Clock

    @Autowired
    lateinit var blogService: BlogService

    @Before
    fun setUp() {
        mockClock = Mockito.mock(Clock::class.java)
        blogService.clock = mockClock
        itIsNow()
    }

    @Test
    fun createAndGetBlogEntry_Published() {
        val now = itIsNow()

        val newBlogEntry = NewBlogEntry(
                title = "Some Blog Entry",
                content = "A bit of content",
                published = true
        )


        val created = blogService.createBlogEntry(newBlogEntry)

        val expectedBlogEntry = BlogEntry(
                id = created.id,
                title = newBlogEntry.title,
                content = newBlogEntry.content,
                published = newBlogEntry.published,
                publishedOn = now,
                updated = now,
                created = now
        )

        Assertions.assertEquals(expectedBlogEntry, created)

        val retrieved = blogService.getBlogEntryById(created.id)

        Assertions.assertEquals(expectedBlogEntry, retrieved)

    }

    @Test
    fun createAndGetBlogEntry_NotPublished() {
        val now = itIsNow()

        val newBlogEntry = NewBlogEntry(
                title = "Some Blog Entry",
                content = "A bit of content",
                published = false
        )


        val created = blogService.createBlogEntry(newBlogEntry)

        val expectedBlogEntry = BlogEntry(
                id = created.id,
                title = newBlogEntry.title,
                content = newBlogEntry.content,
                published = newBlogEntry.published,
                publishedOn = null,
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
    fun update_UnPublishedToPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = NewBlogEntry(
                title = "Some Blog Entry",
                content = "A bit of content",
                published = false
        )

        val created = blogService.createBlogEntry(newBlogEntry)

        val updatedTime = itIsNow(createdTime.plusDays(1))

        val update = created.copy(title = "Changed Title", published = true)

        val expected = update.copy(publishedOn = updatedTime, updated = updatedTime)

        val updated = blogService.updateBlogEntry(update.toUpdate())

        Assertions.assertEquals(expected, updated)
        Assertions.assertEquals(expected, blogService.getBlogEntryById(created.id))
    }

    @Test
    fun update_PublishedStaysPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = NewBlogEntry(
                title = "Some Blog Entry",
                content = "A bit of content",
                published = true
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
    fun update_PublishedToUnPublished_keepLastPublished() {
        val createdTime = itIsNow()

        val newBlogEntry = NewBlogEntry(
                title = "Some Blog Entry",
                content = "A bit of content",
                published = true
        )

        val created = blogService.createBlogEntry(newBlogEntry)

        val updatedTime = itIsNow(createdTime.plusDays(1))

        val update = created.copy(
                title = "Changed Title",
                content = "New Content",
                published = false
        )

        val expected = update.copy(updated = updatedTime)

        val updated = blogService.updateBlogEntry(update.toUpdate())

        Assertions.assertEquals(expected, updated)
        Assertions.assertEquals(expected, blogService.getBlogEntryById(created.id))
    }

    @Test
    fun update_DoesNotExist() {
        Assertions.assertThrows(NoSuchElementException::class.java, {
            blogService.updateBlogEntry(UpdateBlogEntry(-1, "", "", true))
        })
    }

    @Test
    fun getBlogEntryHeaders() {
        val created: List<BlogEntryHeader> = listOf(
                blogService.createBlogEntry(NewBlogEntry("Entry1")).toHeader(),
                blogService.createBlogEntry(NewBlogEntry("Entry2")).toHeader()
        )
        val actual = blogService.getBlogEntryHeaders()
        for (header in created){
            Assertions.assertTrue(actual.contains(header))
        }
    }

    /**
     * Helper for setting the mock time.
     */
    private fun itIsNow(now: ZonedDateTime = LocalDateTime.now().atZone(ZoneOffset.UTC)): ZonedDateTime {
        Mockito.`when`(mockClock.instant()).thenReturn(now.toInstant())
        return now
    }

}