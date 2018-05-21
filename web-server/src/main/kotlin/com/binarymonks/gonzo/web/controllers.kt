package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.blog.api.BlogEntryHeader
import com.binarymonks.gonzo.core.blog.api.BlogEntryNew
import com.binarymonks.gonzo.core.blog.api.BlogEntryUpdate
import com.binarymonks.gonzo.core.blog.service.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class BlogController {

    @Autowired
    lateinit var blogService: BlogService

    @PostMapping("/${Routes.BLOGS}")
    fun createBlogEntry(@RequestBody newBlogEntry: BlogEntryNew): BlogEntry {
        return blogService.createBlogEntry(newBlogEntry)
    }

    @PutMapping("/${Routes.BLOGS}/{id}")
    fun updateBlogEntry(@PathVariable id: Long, @RequestBody update: BlogEntryUpdate): BlogEntry {
        val update = update.copy(id = id)
        return blogService.updateBlogEntry(update)
    }

    @GetMapping("/${Routes.BLOGS}")
    fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        return blogService.getBlogEntryHeaders()
    }

    @GetMapping("/${Routes.BLOGS}/{id}")
    fun getBlogEntryById(@PathVariable id: Long): BlogEntry {
        return blogService.getBlogEntryById(id)
    }

}