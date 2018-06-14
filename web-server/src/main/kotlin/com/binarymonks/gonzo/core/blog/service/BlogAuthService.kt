package com.binarymonks.gonzo.core.blog.service

import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.authz.service.AuthorizedService
import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.common.Actions
import com.binarymonks.gonzo.core.common.Credentials
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

    override fun createBlogEntry(credentials: Credentials, blogEntryNew: BlogEntryNew): BlogEntryDraft {
        checkAuth(credentials, Actions.CREATE, blogEntryNew.attributes())
        return blogService.createBlogEntry(blogEntryNew)
    }

    override fun updateBlogEntry(credentials: Credentials, update: BlogEntryUpdate): BlogEntryDraft {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryDraftByID(credentials: Credentials, blogID: Long): BlogEntryDraft {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publishBlogEntry(credentials: Credentials, blogID: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryById(credentials: Credentials, id: Long): BlogEntry {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryHeaders(credentials: Credentials): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryHeadersByAuthor(credentials: Credentials, authorID: Long): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryDraftHeaders(credentials: Credentials, authorID: Long): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}