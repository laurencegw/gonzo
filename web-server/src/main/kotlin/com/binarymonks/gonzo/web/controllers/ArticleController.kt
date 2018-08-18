package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.articles.api.*
import com.binarymonks.gonzo.core.articles.service.ArticleAuthService
import com.binarymonks.gonzo.web.Routes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ArticleController {

    @Autowired
    lateinit var articleService: ArticleAuthService

    @PostMapping("${Routes.ARTICLES}")
    fun createArticleEntry(@RequestBody newArticle: ArticleDraftNew): ArticleDraft {
        return articleService.createArticleEntry(getCredentials(), newArticle)
    }

    @PutMapping("${Routes.ARTICLES}/{id}")
    fun updateArticleEntry(@PathVariable id: Long, @RequestBody update: ArticleDraftUpdate): ArticleDraft {
        val updateWithPathID = update.copy(id = id)
        return articleService.updateArticleEntry(getCredentials(), updateWithPathID)
    }

    @DeleteMapping("${Routes.ARTICLES}/{id}")
    fun deleteArticleEntry(@PathVariable id: Long) {
        return articleService.deleteArticleEntry(getCredentials(), id)
    }

    @PutMapping("${Routes.ARTICLES}/{id}/publish")
    fun publishArticle(@PathVariable id: Long) {
        return articleService.publishArticleEntry(getCredentials(), id)
    }

    @GetMapping("${Routes.ARTICLES}")
    fun getArticleEntryHeaders(): List<ArticleHeader> {
        return articleService.getArticleEntryHeaders(getCredentials())
    }

    @GetMapping("${Routes.USERS}/{authorID}/drafts")
    fun getArticleEntryDraftHeaders(@PathVariable authorID: Long): List<ArticleHeader> {
        return articleService.getArticleEntryDraftHeaders(getCredentials(), authorID)
    }

    @GetMapping("${Routes.USERS}/{authorID}/articles")
    fun getArticleEntryHeadersByAuthor(@PathVariable authorID: Long): List<ArticleHeader> {
        return articleService.getArticleEntryHeadersByAuthor(getCredentials(), authorID)
    }

    @GetMapping("${Routes.ARTICLES}/{id}/draft")
    fun getArticleEntryDraftByID(@PathVariable id: Long): ArticleDraft {
        return articleService.getArticleEntryDraftByID(getCredentials(), id)
    }

    @GetMapping("${Routes.ARTICLES}/{id}")
    fun getArticleEntryById(@PathVariable id: Long): Article {
        return articleService.getArticleEntryById(getCredentials(), id)
    }

}