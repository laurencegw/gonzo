package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.web.client.getForObject


class BlogClient(baseURL: String) : Blog, AuthClient(baseURL) {

    override fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntry {
        return restTemplate.postForObject(
                "$baseURL/${Routes.BLOGS}",
                HttpEntity(blogEntryNew, createHeaders()),
                BlogEntry::class.java
        )!!
    }

    override fun updateBlogEntry(update: BlogEntryUpdate): BlogEntry {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOGS}/${update.id}",
                HttpMethod.PUT,
                HttpEntity(update, createHeaders()),
                object : ParameterizedTypeReference<BlogEntry>() {}
        )
        return checkNotNull(response.body)
    }

    override fun publishBlogEntry(blogID: Long) {
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
                "$baseURL/${Routes.BLOGS}/$id",
                BlogEntry::class.java
        )!!
    }
}