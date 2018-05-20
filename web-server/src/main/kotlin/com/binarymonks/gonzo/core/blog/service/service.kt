package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.blog.persistence.BlogEntryEntity
import com.binarymonks.gonzo.core.blog.persistence.BlogRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.*

@Service
class BlogService : Blog {

    @Autowired
    lateinit var blogRepo: BlogRepo

    var clock: Clock = java.time.Clock.systemUTC()

    override fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntry = blogRepo.save(BlogEntryEntity(
            title = blogEntryNew.title,
            content = blogEntryNew.content,
            published = blogEntryNew.published,
            firstPublished = if (blogEntryNew.published) now() else null,
            created = now(),
            updated = now()
    )).toBlogEntry()


    override fun updateBlogEntry(update: BlogEntryUpdate): BlogEntry {
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

    override fun getBlogEntryHeaders(): List<BlogEntryHeader> = blogRepo.findAll().map { it.toBlogEntry().toHeader() }

    override fun getBlogEntryById(id: Long): BlogEntry = blogRepo.findById(id).get().toBlogEntry()

    private fun now(): ZonedDateTime = Instant.ofEpochMilli(clock.instant().toEpochMilli()).atZone(ZoneId.of("UTC"))
}
