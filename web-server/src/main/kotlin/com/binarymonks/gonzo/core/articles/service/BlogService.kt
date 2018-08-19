package com.binarymonks.gonzo.core.articles.service

import com.binarymonks.gonzo.core.articles.api.*
import com.binarymonks.gonzo.core.articles.persistence.ArticleDraftEntity
import com.binarymonks.gonzo.core.articles.persistence.ArticlePublished
import com.binarymonks.gonzo.core.articles.persistence.ArticleRepo
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ArticlesService(
        @Autowired
        var articleRepo: ArticleRepo,
        @Autowired
        var userRepo: UserRepo
) : Articles {

    override fun createArticle(articleNew: ArticleDraftNew): ArticleDraft {
        articleNew.validate()
        return articleRepo.save(ArticleDraftEntity(
                title = articleNew.title,
                content = articleNew.content,
                author = userRepo.findById(articleNew.authorID).get(),
                created = nowUTC(),
                updated = nowUTC()
        )).toArticleEntryDraft()
    }

    override fun updateArticle(update: ArticleDraftUpdate): ArticleDraft {
        val entry = articleRepo.findById(update.id).get()
        val changed = listOf(
                entry.publishedArticle?.content != update.content,
                entry.publishedArticle?.title != update.title
        ).any { it }
        if (changed) {
            entry.title = update.title
            entry.content = update.content
            entry.unpublishedChanges = true
            entry.updated = nowUTC()
        }
        return articleRepo.save(entry).toArticleEntryDraft()
    }

    override fun deleteArticle(articleID: Long){
        articleRepo.deleteById(articleID)
    }

    override fun getArticleDraftByID(articleID: Long) = articleRepo.findById(articleID).get().toArticleEntryDraft()

    override fun getArticleDraftHeaders(authorID: Long): List<ArticleHeader> {
        val userEntity = userRepo.findById(authorID).get()
        return articleRepo.findAllByAuthor(userEntity).map { it.toArticleEntryDraft().toHeader() }
    }

    override fun getArticleHeadersByAuthor(authorID: Long): List<ArticleHeader> {
        val userEntity = userRepo.findById(authorID).get()
        val userArticleEntries = articleRepo.findAllByAuthor(userEntity).filter {
            it.publishedArticle != null
        }
        val userHeaders = userArticleEntries.map { it.toArticleEntry().toHeader() }
        return userHeaders
    }

    override fun publishArticle(articleID: Long) {
        val now = nowUTC()
        val entity = articleRepo.findById(articleID).get()
        if (entity.publishedArticle != null) {
            entity.publishedArticle!!.content = entity.content
            entity.publishedArticle!!.title = entity.title
            entity.publishedArticle!!.updated = now
        } else {
            entity.publishedArticle = ArticlePublished(
                    title = entity.title,
                    content = entity.content,
                    created = now,
                    updated = now
            )
        }
        entity.unpublishedChanges = false
        articleRepo.save(entity)
    }

    override fun getArticleHeaders(): List<ArticleHeader> = articleRepo.findAll().filter {
        it.publishedArticle != null
    }.map {
        it.toArticleEntry().toHeader()
    }

    override fun getArticleById(id: Long): Article = articleRepo.findById(id).get().toArticleEntry()

}
