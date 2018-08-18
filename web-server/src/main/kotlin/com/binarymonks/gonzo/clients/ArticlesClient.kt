package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.articles.api.*
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod


class ArticlesClient(baseURL: String) : Articles, AuthClient(baseURL) {

    override fun createArticleEntry(articleNew: ArticleDraftNew): ArticleDraft {
        return restTemplate.postForObject(
                "$baseURL/${Routes.ARTICLES}",
                HttpEntity(articleNew, createHeaders()),
                ArticleDraft::class.java
        )!!
    }

    override fun updateArticleEntry(update: ArticleDraftUpdate): ArticleDraft {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.ARTICLES}/${update.id}",
                HttpMethod.PUT,
                HttpEntity(update, createHeaders()),
                object : ParameterizedTypeReference<ArticleDraft>() {}
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

    override fun getArticleEntryDraftByID(articleID: Long): ArticleDraft {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.articleDraft(articleID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<ArticleDraft>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryHeadersByAuthor(authorID: Long): List<ArticleHeader> {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.userArticles(authorID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<List<ArticleHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryDraftHeaders(authorID: Long): List<ArticleHeader> {
        val response =  restTemplate.exchange(
                "$baseURL/${Routes.userDrafts(authorID)}",
                HttpMethod.GET,
                HttpEntity(null,createHeaders()),
                object : ParameterizedTypeReference<List<ArticleHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryHeaders(): List<ArticleHeader> {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.ARTICLES}",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<ArticleHeader>>() {}
        )
        return checkNotNull(response.body)
    }

    override fun getArticleEntryById(id: Long): Article {
        return restTemplate.getForObject(
                "$baseURL/${Routes.articleEntry(id)}",
                Article::class.java
        )!!
    }
}