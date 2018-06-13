package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.blog.persistence.BlogEntryDraftEntity
import com.binarymonks.gonzo.core.blog.persistence.BlogEntryPublished
import com.binarymonks.gonzo.core.blog.persistence.BlogRepo
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BlogService : Blog {

    @Autowired
    lateinit var blogRepo: BlogRepo

    @Autowired
    lateinit var userRepo: UserRepo


    override fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntryDraft {

        return blogRepo.save(BlogEntryDraftEntity(
                title = blogEntryNew.title,
                content = blogEntryNew.content,
                author = userRepo.findById(blogEntryNew.authorID).get(),
                created = nowUTC(),
                updated = nowUTC()
        )).toBlogEntryDraft()
    }


    override fun updateBlogEntry(update: BlogEntryUpdate): BlogEntryDraft {
        val entry = blogRepo.findById(update.id).get()
        val changed = listOf(
                entry.publishedBlog?.content != update.content,
                entry.publishedBlog?.title != update.title
        ).any { it }
        if (changed) {
            entry.title = update.title
            entry.content = update.content
            entry.unpublishedChanges = true
            entry.updated = nowUTC()
        }
        return blogRepo.save(entry).toBlogEntryDraft()
    }

    override fun getBlogEntryDraftByID(blogID: Long) = blogRepo.findById(blogID).get().toBlogEntryDraft()

    override fun getBlogEntryDraftHeaders(authorID: Long): List<BlogEntryHeader> {
        val userEntity = userRepo.findById(authorID).get()
        return blogRepo.findAllByAuthor(userEntity).map { it.toBlogEntryDraft().toHeader() }
    }

    override fun getBlogEntryHeadersByAuthor(authorID: Long): List<BlogEntryHeader> {
        val userEntity = userRepo.findById(authorID).get()
        return blogRepo.findAllByAuthor(userEntity).filter {
            it.publishedBlog!=null }.map {
            it.toBlogEntryDraft().toHeader() }
    }

    override fun publishBlogEntry(blogID: Long) {
        val now = nowUTC()
        val entity = blogRepo.findById(blogID).get()
        if (entity.publishedBlog != null) {
            entity.publishedBlog!!.content = entity.content
            entity.publishedBlog!!.title = entity.title
            entity.publishedBlog!!.updated = now
        } else {
            entity.publishedBlog = BlogEntryPublished(
                    blogEntryDraftID = entity.id!!,
                    title = entity.title,
                    content = entity.content,
                    created = now,
                    updated = now
            )
        }
        entity.unpublishedChanges = false
        blogRepo.save(entity)
    }

    override fun getBlogEntryHeaders(): List<BlogEntryHeader> = blogRepo.findAll().filter {
        it.publishedBlog!=null }.map {
        it.toBlogEntryDraft().toHeader() }

    override fun getBlogEntryById(id: Long): BlogEntry = blogRepo.findById(id).get().toBlogEntry()

}
