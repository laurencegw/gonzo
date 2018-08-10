package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.authz.service.AuthorizedService
import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.common.Actions
import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.Types
import com.binarymonks.gonzo.core.users.service.SignInService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BlogAuthService(
        @Autowired signInService: SignInService,
        @Autowired accessDecisionService: AccessDecisionService
) : BlogAuth, AuthorizedService(signInService, accessDecisionService) {

    @Autowired
    lateinit var blogService: BlogService

    override fun createBlogEntry(credentials: Credentials, blogEntryNew: BlogDraftEntryNew): BlogEntryDraft {
        checkAuth(credentials, Actions.CREATE, blogEntryNew.attributes())
        return blogService.createBlogEntry(blogEntryNew)
    }

    override fun updateBlogEntry(credentials: Credentials, update: BlogDraftEntryUpdate): BlogEntryDraft {
        val blogToUpdate = blogService.getBlogEntryDraftByID(update.id)
        checkAuth(credentials, Actions.MODIFY, blogToUpdate.attributes())
        return blogService.updateBlogEntry(update)
    }

    override fun deleteBlogEntry(credentials: Credentials, blogID: Long) {

    }

    override fun getBlogEntryDraftByID(credentials: Credentials, blogID: Long): BlogEntryDraft {
        val blogDraft = blogService.getBlogEntryDraftByID(blogID)
        checkAuth(credentials, Actions.READ, blogDraft.attributes())
        return blogDraft
    }

    override fun publishBlogEntry(credentials: Credentials, blogID: Long) {
        val blogToPublish = blogService.getBlogEntryDraftByID(blogID)
        checkAuth(credentials, Actions.MODIFY, blogToPublish.attributes())
        return blogService.publishBlogEntry(blogID)
    }

    override fun getBlogEntryById(credentials: Credentials, id: Long): BlogEntry {
        val blog = blogService.getBlogEntryById(id)
        checkAuth(credentials, Actions.READ, blog.attributes())
        return blog
    }

    override fun getBlogEntryHeaders(credentials: Credentials): List<BlogEntryHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.BLOG)))
        return blogService.getBlogEntryHeaders()
    }

    override fun getBlogEntryHeadersByAuthor(credentials: Credentials, authorID: Long): List<BlogEntryHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.BLOG), Pair("authorID", authorID)))
        return blogService.getBlogEntryHeadersByAuthor(authorID)
    }

    override fun getBlogEntryDraftHeaders(credentials: Credentials, authorID: Long): List<BlogEntryHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.BLOG_DRAFT), Pair("authorID", authorID)))
        return blogService.getBlogEntryDraftHeaders(authorID)
    }
}