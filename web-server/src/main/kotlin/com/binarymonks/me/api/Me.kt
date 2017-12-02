package com.binarymonks.me.api

import com.binarymonks.me.core.blog.api.BlogEntry
import com.binarymonks.me.core.blog.api.BlogEntryHeader
import com.binarymonks.me.core.blog.api.NewBlogEntry
import com.binarymonks.me.core.blog.api.UpdateBlogEntry


interface Me {

    fun createBlogEntry(credentials: Credentials, newBlogEntry: NewBlogEntry): BlogEntry
    fun updateBlogEntry(credentials: Credentials, update: UpdateBlogEntry): BlogEntry
    fun getBlogEntryHeaders(credentials: Credentials): List<BlogEntryHeader>
    fun getBlogEntryById(credentials: Credentials, id: Long): BlogEntry

}


sealed class Credentials

class NullCreds : Credentials()