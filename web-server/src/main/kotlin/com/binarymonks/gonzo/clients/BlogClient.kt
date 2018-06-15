package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod


class BlogClient(baseURL: String) : Blog, AuthClient(baseURL) {

    override fun createBlogEntry(blogEntryNew: BlogDraftEntryNew): BlogEntryDraft {
        return restTemplate.postForObject(
                "$baseURL/${Routes.BLOGS}",
                HttpEntity(blogEntryNew, createHeaders()),
                BlogEntryDraft::class.java
        )!!
    }

    override fun updateBlogEntry(update: BlogDraftEntryUpdate): BlogEntryDraft {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOGS}/${update.id}",
                HttpMethod.PUT,
                HttpEntity(update, createHeaders()),
                object : ParameterizedTypeReference<BlogEntryDraft>() {}
        )
        return checkNotNull(response.body)
    }

    override fun publishBlogEntry(blogID: Long) {
        restTemplate.put(
                "$baseURL/${Routes.publishBlog(blogID)}",
                HttpEntity(null,createHeaders())
        )
    }

    override fun getBlogEntryDraftByID(blogID: Long): BlogEntryDraft {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.blogDraft(blogID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<BlogEntryDraft>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getBlogEntryHeadersByAuthor(authorID: Long): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryDraftHeaders(authorID: Long): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOGS}",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<BlogEntryHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getBlogEntryById(id: Long): BlogEntry {
        return restTemplate.getForObject(
                "$baseURL/${Routes.blogEntry(id)}",
                BlogEntry::class.java
        )!!
    }
}