package com.binarymonks.me.clients

import com.binarymonks.me.core.blog.api.*
import com.binarymonks.me.web.Routes
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod


class BlogClient(val baseURL: String) : Blog {
    private val restTemplate = RestTemplateBuilder().rootUri(baseURL).build()!!

    override fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntry {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOG}",
                HttpMethod.POST,
                HttpEntity(blogEntryNew),
                object : ParameterizedTypeReference<BlogEntry>() {}
        )
        return response.body
    }

    override fun updateBlogEntry(update: BlogEntryUpdate): BlogEntry {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOG}/${update.id}",
                HttpMethod.PUT,
                HttpEntity(update),
                object : ParameterizedTypeReference<BlogEntry>() {}
        )
        return response.body
    }

    override fun getBlogEntryHeaders(): List<BlogEntryHeader> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlogEntryById(id: Long): BlogEntry {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}