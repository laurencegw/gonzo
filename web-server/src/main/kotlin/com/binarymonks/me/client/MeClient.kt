package com.binarymonks.me.client

import com.binarymonks.me.api.Credentials
import com.binarymonks.me.api.Me
import com.binarymonks.me.core.blog.api.BlogEntry
import com.binarymonks.me.core.blog.api.BlogEntryHeader
import com.binarymonks.me.core.blog.api.NewBlogEntry
import com.binarymonks.me.core.blog.api.UpdateBlogEntry


class MeClient : Me {

    override fun createBlogEntry(credentials: Credentials, newBlogEntry: NewBlogEntry): BlogEntry {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlogEntry(credentials: Credentials, update: UpdateBlogEntry): BlogEntry {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryHeaders(credentials: Credentials): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryById(credentials: Credentials, id: Long): BlogEntry {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}