package com.binarymonks.me.web

import com.binarymonks.me.core.blog.api.BlogEntry
import com.binarymonks.me.core.blog.api.BlogEntryHeader
import com.binarymonks.me.core.blog.api.BlogEntryNew
import com.binarymonks.me.core.blog.api.BlogEntryUpdate
import com.binarymonks.me.core.blog.service.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class BlogController {

    @Autowired
    lateinit var blogService: BlogService

    @PostMapping(value = "/${Routes.BLOGS}")
    fun createBlogEntry(@RequestBody newBlogEntry: BlogEntryNew): BlogEntry {
        return blogService.createBlogEntry(newBlogEntry)
    }

    @PutMapping(value = "/${Routes.BLOGS}/{id}")
    fun updateBlogEntry(@PathVariable id: Long, @RequestBody update: BlogEntryUpdate): BlogEntry {
        val update = update.copy(id = id)
        return blogService.updateBlogEntry(update)
    }

    @GetMapping(value = "/${Routes.BLOGS}")
    fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        return blogService.getBlogEntryHeaders()
    }

    @GetMapping(value = "/${Routes.BLOGS}/{id}")
    fun getBlogEntryById(@PathVariable id: Long): BlogEntry {
        return blogService.getBlogEntryById(id)
    }

}