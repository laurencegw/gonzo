package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.article.api.*
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod


class ArticleClient(baseURL: String) : Article, AuthClient(baseURL) {

    override fun createArticleEntry(articleEntryNew: ArticleDraftEntryNew): ArticleEntryDraft {
        return restTemplate.postForObject(
                "$baseURL/${Routes.ARTICLES}",
                HttpEntity(articleEntryNew, createHeaders()),
                ArticleEntryDraft::class.java
        )!!
    }

    override fun updateArticleEntry(update: ArticleDraftEntryUpdate): ArticleEntryDraft {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.ARTICLES}/${update.id}",
                HttpMethod.PUT,
                HttpEntity(update, createHeaders()),
                object : ParameterizedTypeReference<ArticleEntryDraft>() {}
        )
        return checkNotNull(response.body)
    }

    override fun publishArticleEntry(articleID: Long) {
        restTemplate.put(
                "$baseURL/${Routes.publishArticle(articleID)}",
                HttpEntity(null,createHeaders())
        )
    }

    override fun deleteArticleEntry(articleID: Long) {
        restTemplate.exchange(
                "$baseURL/${Routes.articleEntry(articleID)}",
                HttpMethod.DELETE,
                HttpEntity(null,createHeaders()),
                String::class.java
        )
    }

    override fun getArticleEntryDraftByID(articleID: Long): ArticleEntryDraft {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.articleDraft(articleID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<ArticleEntryDraft>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryHeadersByAuthor(authorID: Long): List<ArticleEntryHeader> {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.userArticles(authorID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<List<ArticleEntryHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryDraftHeaders(authorID: Long): List<ArticleEntryHeader> {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.userDrafts(authorID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<List<ArticleEntryHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryHeaders(): List<ArticleEntryHeader> {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.ARTICLES}",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<ArticleEntryHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryById(id: Long): ArticleEntry {
        return restTemplate.getForObject(
                "$baseURL/${Routes.articleEntry(id)}",
                ArticleEntry::class.java
        )!!
    }
}