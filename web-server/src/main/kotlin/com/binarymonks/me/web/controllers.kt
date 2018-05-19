package com.binarymonks.me.web

import com.binarymonks.me.core.blog.api.BlogEntry
import com.binarymonks.me.core.blog.api.BlogEntryNew
import com.binarymonks.me.core.blog.api.BlogEntryUpdate
import com.binarymonks.me.core.blog.service.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class BlogController {

    @Autowired
    lateinit var blogService: BlogService

    @PostMapping(
            value = "/${Routes.BLOG}"
    )
    fun createBlogEntry(@RequestBody newBlogEntry: BlogEntryNew):BlogEntry{
        return blogService.createBlogEntry(newBlogEntry)
    }

    @PutMapping(
            value = "/${Routes.BLOG}/{id}"
    )
    fun updateBlogEntry(@PathVariable id: Long, @RequestBody update: BlogEntryUpdate):BlogEntry{
        val update = update.copy(id=id)
        return blogService.updateBlogEntry(update)
    }

}