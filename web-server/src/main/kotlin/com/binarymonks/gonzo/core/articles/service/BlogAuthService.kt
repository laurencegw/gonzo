package com.binarymonks.gonzo.core.articles.service

import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.authz.service.AuthorizedService
import com.binarymonks.gonzo.core.articles.api.*
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
    lateinit var articleService: ArticlesService

    override fun createArticleEntry(credentials: Credentials, articleNew: ArticleDraftNew): ArticleDraft {
        checkAuth(credentials, Actions.CREATE, articleNew.attributes())
        return articleService.createArticleEntry(articleNew)
    }

    override fun updateArticleEntry(credentials: Credentials, update: ArticleDraftUpdate): ArticleDraft {
        val articleToUpdate = articleService.getArticleEntryDraftByID(update.id)
        checkAuth(credentials, Actions.MODIFY, articleToUpdate.attributes())
        return articleService.updateArticleEntry(update)
    }

    override fun deleteArticleEntry(credentials: Credentials, articleID: Long) {
        val articleToDelete = articleService.getArticleEntryDraftByID(articleID)
        checkAuth(credentials, Actions.DELETE, articleToDelete.attributes())
        articleService.deleteArticleEntry(articleID)
    }

    override fun getArticleEntryDraftByID(credentials: Credentials, articleID: Long): ArticleDraft {
        val articleDraft = articleService.getArticleEntryDraftByID(articleID)
        checkAuth(credentials, Actions.READ, articleDraft.attributes())
        return articleDraft
    }

    override fun publishArticleEntry(credentials: Credentials, articleID: Long) {
        val articleToPublish = articleService.getArticleEntryDraftByID(articleID)
        checkAuth(credentials, Actions.MODIFY, articleToPublish.attributes())
        return articleService.publishArticleEntry(articleID)
    }

    override fun getArticleEntryById(credentials: Credentials, id: Long): Article {
        val article = articleService.getArticleEntryById(id)
        checkAuth(credentials, Actions.READ, article.attributes())
        return article
    }

    override fun getArticleEntryHeaders(credentials: Credentials): List<ArticleHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE)))
        return articleService.getArticleEntryHeaders()
    }

    override fun getArticleEntryHeadersByAuthor(credentials: Credentials, authorID: Long): List<ArticleHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE), Pair("authorID", authorID)))
        return articleService.getArticleEntryHeadersByAuthor(authorID)
    }

    override fun getArticleEntryDraftHeaders(credentials: Credentials, authorID: Long): List<ArticleHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE_DRAFT), Pair("authorID", authorID)))
        return articleService.getArticleEntryDraftHeaders(authorID)
    }
}