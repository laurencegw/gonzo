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
    fun createArticle(@RequestBody newArticle: ArticleDraftNew): ArticleDraft {
        return articleService.createArticle(getCredentials(), newArticle)
    }

    @PutMapping("${Routes.ARTICLES}/{id}")
    fun updateArticle(@PathVariable id: Long, @RequestBody update: ArticleDraftUpdate): ArticleDraft {
        val updateWithPathID = update.copy(id = id)
        return articleService.updateArticle(getCredentials(), updateWithPathID)
    }

    @DeleteMapping("${Routes.ARTICLES}/{id}")
    fun deleteArticle(@PathVariable id: Long) {
        return articleService.deleteArticle(getCredentials(), id)
    }

    @PutMapping("${Routes.ARTICLES}/{id}/publish")
    fun publishArticle(@PathVariable id: Long) {
        return articleService.publishArticle(getCredentials(), id)
    }

    @GetMapping("${Routes.ARTICLES}")
    fun getArticleHeaders(): List<ArticleHeader> {
        return articleService.getArticleHeaders(getCredentials())
    }

    @GetMapping("${Routes.USERS}/{authorID}/drafts")
    fun getArticleDraftHeaders(@PathVariable authorID: Long): List<ArticleHeader> {
        return articleService.getArticleDraftHeaders(getCredentials(), authorID)
    }

    @GetMapping("${Routes.USERS}/{authorID}/articles")
    fun getArticleHeadersByAuthor(@PathVariable authorID: Long): List<ArticleHeader> {
        return articleService.getArticleHeadersByAuthor(getCredentials(), authorID)
    }

    @GetMapping("${Routes.ARTICLES}/{id}/draft")
    fun getArticleDraftByID(@PathVariable id: Long): ArticleDraft {
        return articleService.getArticleDraftByID(getCredentials(), id)
    }

    @GetMapping("${Routes.ARTICLES}/{id}")
    fun getArticleById(@PathVariable id: Long): Article {
        return articleService.getArticleById(getCredentials(), id)
    }

}