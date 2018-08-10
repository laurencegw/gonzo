package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.blog.service.BlogAuthService
import com.binarymonks.gonzo.web.Routes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class BlogController {

    @Autowired
    lateinit var blogService: BlogAuthService

    @PostMapping("${Routes.BLOGS}")
    fun createBlogEntry(@RequestBody newBlogEntry: BlogDraftEntryNew): BlogEntryDraft {
        return blogService.createBlogEntry(getCredentials(), newBlogEntry)
    }

    @PutMapping("${Routes.BLOGS}/{id}")
    fun updateBlogEntry(@PathVariable id: Long, @RequestBody update: BlogDraftEntryUpdate): BlogEntryDraft {
        val updateWithPathID = update.copy(id = id)
        return blogService.updateBlogEntry(getCredentials(), updateWithPathID)
    }

    @DeleteMapping("${Routes.BLOGS}/{id}")
    fun deleteBlogEntry(@PathVariable id: Long) {
        return blogService.deleteBlogEntry(getCredentials(), id)
    }

    @PutMapping("${Routes.BLOGS}/{id}/publish")
    fun publishBlog(@PathVariable id: Long) {
        return blogService.publishBlogEntry(getCredentials(), id)
    }

    @GetMapping("${Routes.BLOGS}")
    fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        return blogService.getBlogEntryHeaders(getCredentials())
    }

    @GetMapping("${Routes.USERS}/{authorID}/drafts")
    fun getBlogEntryDraftHeaders(@PathVariable authorID: Long): List<BlogEntryHeader> {
        return blogService.getBlogEntryDraftHeaders(getCredentials(), authorID)
    }

    @GetMapping("${Routes.USERS}/{authorID}/blogs")
    fun getBlogEntryHeadersByAuthor(@PathVariable authorID: Long): List<BlogEntryHeader> {
        return blogService.getBlogEntryHeadersByAuthor(getCredentials(), authorID)
    }

    @GetMapping("${Routes.BLOGS}/{id}/draft")
    fun getBlogEntryDraftByID(@PathVariable id: Long): BlogEntryDraft {
        return blogService.getBlogEntryDraftByID(getCredentials(), id)
    }

    @GetMapping("${Routes.BLOGS}/{id}")
    fun getBlogEntryById(@PathVariable id: Long): BlogEntry {
        return blogService.getBlogEntryById(getCredentials(), id)
    }

}