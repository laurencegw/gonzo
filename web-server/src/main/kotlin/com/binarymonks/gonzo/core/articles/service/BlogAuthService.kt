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

    override fun createArticle(credentials: Credentials, articleNew: ArticleDraftNew): ArticleDraft {
        checkAuth(credentials, Actions.CREATE, articleNew.attributes())
        return articleService.createArticle(articleNew)
    }

    override fun updateArticle(credentials: Credentials, update: ArticleDraftUpdate): ArticleDraft {
        val articleToUpdate = articleService.getArticleDraftByID(update.id)
        checkAuth(credentials, Actions.MODIFY, articleToUpdate.attributes())
        return articleService.updateArticle(update)
    }

    override fun deleteArticle(credentials: Credentials, articleID: Long) {
        val articleToDelete = articleService.getArticleDraftByID(articleID)
        checkAuth(credentials, Actions.DELETE, articleToDelete.attributes())
        articleService.deleteArticle(articleID)
    }

    override fun getArticleDraftByID(credentials: Credentials, articleID: Long): ArticleDraft {
        val articleDraft = articleService.getArticleDraftByID(articleID)
        checkAuth(credentials, Actions.READ, articleDraft.attributes())
        return articleDraft
    }

    override fun publishArticle(credentials: Credentials, articleID: Long) {
        val articleToPublish = articleService.getArticleDraftByID(articleID)
        checkAuth(credentials, Actions.MODIFY, articleToPublish.attributes())
        return articleService.publishArticle(articleID)
    }

    override fun getArticleById(credentials: Credentials, id: Long): Article {
        val article = articleService.getArticleById(id)
        checkAuth(credentials, Actions.READ, article.attributes())
        return article
    }

    override fun getArticleHeaders(credentials: Credentials): List<ArticleHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE)))
        return articleService.getArticleHeaders()
    }

    override fun getArticleHeadersByAuthor(credentials: Credentials, authorID: Long): List<ArticleHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE), Pair("authorID", authorID)))
        return articleService.getArticleHeadersByAuthor(authorID)
    }

    override fun getArticleDraftHeaders(credentials: Credentials, authorID: Long): List<ArticleHeader> {
        checkAuth(credentials, Actions.READ, mapOf(Pair("type", Types.ARTICLE_DRAFT), Pair("authorID", authorID)))
        return articleService.getArticleDraftHeaders(authorID)
    }
}