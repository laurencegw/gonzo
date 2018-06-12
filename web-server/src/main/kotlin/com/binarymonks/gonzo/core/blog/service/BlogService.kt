package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.blog.persistence.BlogEntryDraftEntity
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
        TODO()
//        val entity = blogRepo.findById(update.id).get()
//        entity.title = update.title
//        entity.content = update.content
//        entity.updated = nowUTC()
//        blogRepo.save(entity)
//        return entity.toBlogEntry()
    }

    override fun getBlogEntryDraftByID(blogID: Long) = blogRepo.findById(blogID).get().toBlogEntryDraft()

    override fun getBlogEntryDraftHeaders(publisherID: Long): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publishBlogEntry(blogID: Long) {
        TODO()
//        val entity = blogRepo.findById(blogID).get()
//        entity.published = true
//        entity.created = nowUTC()
//        blogRepo.save(entity)
    }

    override fun getBlogEntryHeaders(): List<BlogEntryHeader> = blogRepo.findAll().map { it.toBlogEntryDraft().toHeader() }

    override fun getBlogEntryById(id: Long): BlogEntry = blogRepo.findById(id).get().toBlogEntry()

}
