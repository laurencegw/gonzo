package com.binarymonks.gonzo.core.article.service

import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.authz.service.AuthorizedService
import com.binarymonks.gonzo.core.article.api.*
import com.binarymonks.gonzo.core.common.Actions
import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.Types
import com.binarymonks.gonzo.core.users.service.SignInService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleAuthService(
        @Autowired signInService: SignInService,
        @Autowired accessDecisionService: AccessDecisionService
) : ArticleAuth, AuthorizedService(signInService, accessDecisionService) {

    @Autowired
    lateinit var articleService: ArticleService

    override fun createArticleEntry(credentials: Credentials, articleEntryNew: ArticleDraftEntryNew): ArticleEntryDraft {
        checkAuth(credentials, Actions.CREATE, articleEntryNew.attributes())
        return articleService.createArticleEntry(articleEntryNew)
    }

    override fun updateArticleEntry(credentials: Credentials, update: ArticleDraftEntryUpdate): ArticleEntryDraft {
        val articleToUpdate = articleService.getArticleEntryDraftByID(update.id)
        checkAuth(credentials, Actions.MODIFY, articleToUpdate.attributes())
        return articleService.updateArticleEntry(update)
    }

    override fun deleteArticleEntry(credentials: Credentials, articleID: Long) {
        val articleToDelete = articleService.getArticleEntryDraftByID(articleID)
        checkAuth(credentials, Actions.DELETE, articleToDelete.attributes())
        articleService.deleteArticleEntry(articleID)
    }

    override fun getArticleEntryDraftByID(credentials: Credentials, articleID: Long): ArticleEntryDraft {
        val articleDraft = articleService.getArticleEntryDraftByID(articleID)
        checkAuth(credentials, Actions.READ, articleDraft.attributes())
        return articleDraft
    }

    override fun publishArticleEntry(credentials: Credentials, articleID: Long) {
        val articleToPublish = articleService.getArticleEntryDraftByID(articleID)
        checkAuth(credentials, Actions.MODIFY, articleToPublish.attributes())
        return articleService.publishArticleEntry(articleID)
    }

    override fun getArticleEntryById(credentials: Credentials, id: Long): ArticleEntry {
        val article = articleService.getArticleEntryById(id)
        checkAuth(credentials, Actions.READ, article.attributes())
        return article
    }

    override fun getArticleEntryHeaders(credentials: Credentials): List<ArticleEntryHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE)))
        return articleService.getArticleEntryHeaders()
    }

    override fun getArticleEntryHeadersByAuthor(credentials: Credentials, authorID: Long): List<ArticleEntryHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE), Pair("authorID", authorID)))
        return articleService.getArticleEntryHeadersByAuthor(authorID)
    }

    override fun getArticleEntryDraftHeaders(credentials: Credentials, authorID: Long): List<ArticleEntryHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE_DRAFT), Pair("authorID", authorID)))
        return articleService.getArticleEntryDraftHeaders(authorID)
    }
}