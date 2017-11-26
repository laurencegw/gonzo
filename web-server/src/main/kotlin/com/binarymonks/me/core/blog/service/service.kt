package com.binarymonks.me.core.blog.service

import com.binarymonks.me.core.blog.api.*
import com.binarymonks.me.core.blog.persistence.BlogEntryEntity
import com.binarymonks.me.core.blog.persistence.BlogRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class BlogService : Blog {

    @Autowired
    lateinit var blogRepo: BlogRepo

    var clock: Clock = java.time.Clock.systemUTC()

    override fun createBlogEntry(newBlogEntry: NewBlogEntry): BlogEntry {
        val saved = blogRepo.save(BlogEntryEntity(
                title = newBlogEntry.title,
                content = newBlogEntry.content,
                published = newBlogEntry.published,
                firstPublished = if (newBlogEntry.published) now() else null,
                created = now(),
                updated = now()
        ))
        return saved.toBlogEntry()
    }

    override fun updateBlogEntry(update: UpdateBlogEntry): BlogEntry {
        val entity = blogRepo.findById(update.id).get()
        entity.title = update.title
        entity.content = update.content
        if (!entity.published && update.published) {
            entity.firstPublished = now()
        }
        entity.published = update.published
        entity.updated = now()
        blogRepo.save(entity)
        return entity.toBlogEntry()
    }

    override fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryById(id: Long): BlogEntry {
        val option = blogRepo.findById(id)
        val entity = option.get()
        return entity.toBlogEntry()
    }

    private fun now(): ZonedDateTime = Instant.ofEpochMilli(clock.instant().toEpochMilli()).atZone(ZoneOffset.UTC)
}
