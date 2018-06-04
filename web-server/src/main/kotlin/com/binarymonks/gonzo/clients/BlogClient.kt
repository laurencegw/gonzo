package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.blog.api.*
import com.binarymonks.gonzo.core.users.api.LoginCredentials
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod


class BlogClient(private val baseURL: String) : Blog {
    private val restTemplate = restTemplateWithErrorHandler()
    private var token: String = ""

    fun createHeaders(): HttpHeaders {
        return object : HttpHeaders() {
            init {
                val authHeader = "Bearer " + token
                set("Authorization", authHeader)
            }
        }
    }

    fun signIn(email: String, password: String) {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.LOGIN}",
                HttpMethod.POST,
                HttpEntity(LoginCredentials(
                        email = email,
                        password = password
                )),
                object : ParameterizedTypeReference<String>() {}
        )
        token = response.body
    }

    override fun createBlogEntry(blogEntryNew: BlogEntryNew): BlogEntry {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOGS}",
                HttpMethod.POST,
                HttpEntity(blogEntryNew, createHeaders()),
                object : ParameterizedTypeReference<BlogEntry>() {}
        )
        return checkNotNull(response.body)
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
        val response = restTemplate.exchange(
                "$baseURL/${Routes.BLOGS}/$id",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<BlogEntry>() {}
        )
        return checkNotNull(response.body)
    }
}