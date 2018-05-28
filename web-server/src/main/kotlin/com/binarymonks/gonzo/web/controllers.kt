package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.core.blog.api.BlogEntry
import com.binarymonks.gonzo.core.blog.api.BlogEntryHeader
import com.binarymonks.gonzo.core.blog.api.BlogEntryNew
import com.binarymonks.gonzo.core.blog.api.BlogEntryUpdate
import com.binarymonks.gonzo.core.blog.service.BlogService
import com.binarymonks.gonzo.core.users.api.LoginCredentials
import com.binarymonks.gonzo.core.users.service.SignInService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView


@RestController
class SPAProxyController {

    @RequestMapping(
            "/home/**",
            "/blog/**"
    )
    fun forward(model: ModelMap): ModelAndView {
        return ModelAndView("forward:/")
    }
}

@RestController
class BlogController {

    @Autowired
    lateinit var blogService: BlogService

    @PostMapping("${Routes.BLOGS}")
    fun createBlogEntry(@RequestBody newBlogEntry: BlogEntryNew): BlogEntry {
        return blogService.createBlogEntry(newBlogEntry)
    }

    @PutMapping("${Routes.BLOGS}/{id}")
    fun updateBlogEntry(@PathVariable id: Long, @RequestBody update: BlogEntryUpdate): BlogEntry {
        val updateWithPathID = update.copy(id = id)
        return blogService.updateBlogEntry(updateWithPathID)
    }

    @GetMapping("${Routes.BLOGS}")
    fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        return blogService.getBlogEntryHeaders()
    }

    @GetMapping("${Routes.BLOGS}/{id}")
    fun getBlogEntryById(@PathVariable id: Long): BlogEntry {
        return blogService.getBlogEntryById(id)
    }

}

@RestController
class SignInController {
    @Autowired
    lateinit var signInService: SignInService

    @PostMapping("/${Routes.LOGIN}")
    fun login(@RequestBody loginCredentials: LoginCredentials): String {
        return signInService.login(loginCredentials)
    }

}