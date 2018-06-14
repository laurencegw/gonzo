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
    fun createBlogEntry(@RequestBody newBlogEntry: BlogEntryNew): BlogEntryDraft {
        return blogService.createBlogEntry(getCredentials(),newBlogEntry)
    }

    @PutMapping("${Routes.BLOGS}/{id}")
    fun updateBlogEntry(@PathVariable id: Long, @RequestBody update: BlogEntryUpdate): BlogEntryDraft {
        val updateWithPathID = update.copy(id = id)
        return blogService.updateBlogEntry(getCredentials(),updateWithPathID)
    }

    @GetMapping("${Routes.BLOGS}")
    fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        return blogService.getBlogEntryHeaders(getCredentials())
    }

    @GetMapping("${Routes.BLOGS}/{id}")
    fun getBlogEntryById(@PathVariable id: Long): BlogEntry {
        return blogService.getBlogEntryById(getCredentials(), id)
    }

}