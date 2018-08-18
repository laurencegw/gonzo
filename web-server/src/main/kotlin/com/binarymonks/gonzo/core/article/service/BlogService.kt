package com.binarymonks.gonzo.core.article.service

import com.binarymonks.gonzo.core.article.api.*
import com.binarymonks.gonzo.core.article.persistence.ArticleEntryDraftEntity
import com.binarymonks.gonzo.core.article.persistence.ArticleEntryPublished
import com.binarymonks.gonzo.core.article.persistence.ArticleRepo
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ArticleService(
        @Autowired
        var articleRepo: ArticleRepo,
        @Autowired
        var userRepo: UserRepo
) : Article {

    override fun createArticleEntry(articleEntryNew: ArticleDraftEntryNew): ArticleEntryDraft {
        articleEntryNew.validate()
        return articleRepo.save(ArticleEntryDraftEntity(
                title = articleEntryNew.title,
                content = articleEntryNew.content,
                author = userRepo.findById(articleEntryNew.authorID).get(),
                created = nowUTC(),
                updated = nowUTC()
        )).toArticleEntryDraft()
    }

    override fun updateArticleEntry(update: ArticleDraftEntryUpdate): ArticleEntryDraft {
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

    override fun deleteArticleEntry(articleID: Long){
        articleRepo.deleteById(articleID)
    }

    override fun getArticleEntryDraftByID(articleID: Long) = articleRepo.findById(articleID).get().toArticleEntryDraft()

    override fun getArticleEntryDraftHeaders(authorID: Long): List<ArticleEntryHeader> {
        val userEntity = userRepo.findById(authorID).get()
        return articleRepo.findAllByAuthor(userEntity).map { it.toArticleEntryDraft().toHeader() }
    }

    override fun getArticleEntryHeadersByAuthor(authorID: Long): List<ArticleEntryHeader> {
        val userEntity = userRepo.findById(authorID).get()
        val userArticleEntries = articleRepo.findAllByAuthor(userEntity).filter {
            it.publishedArticle != null
        }
        val userHeaders = userArticleEntries.map { it.toArticleEntry().toHeader() }
        return userHeaders
    }

    override fun publishArticleEntry(articleID: Long) {
        val now = nowUTC()
        val entity = articleRepo.findById(articleID).get()
        if (entity.publishedArticle != null) {
            entity.publishedArticle!!.content = entity.content
            entity.publishedArticle!!.title = entity.title
            entity.publishedArticle!!.updated = now
        } else {
            entity.publishedArticle = ArticleEntryPublished(
                    title = entity.title,
                    content = entity.content,
                    created = now,
                    updated = now
            )
        }
        entity.unpublishedChanges = false
        articleRepo.save(entity)
    }

    override fun getArticleEntryHeaders(): List<ArticleEntryHeader> = articleRepo.findAll().filter {
        it.publishedArticle != null
    }.map {
        it.toArticleEntry().toHeader()
    }

    override fun getArticleEntryById(id: Long): ArticleEntry = articleRepo.findById(id).get().toArticleEntry()

}
