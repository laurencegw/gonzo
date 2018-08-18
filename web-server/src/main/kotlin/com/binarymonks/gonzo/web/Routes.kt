package com.binarymonks.gonzo.web

object Routes {
      const val API = "/api"
      const val LOGIN = "$API/login"

      const val ARTICLES = "$API/articles"
      fun articleEntry(articleID: Long) = "$ARTICLES/$articleID"
      fun publishArticle(articleID: Long) = "${articleEntry(articleID)}/publish"
      fun articleDraft(articleID: Long) = "${articleEntry(articleID)}/draft"

      const val USERS = "$API/users"
      const val ME = "$USERS/me"
      fun user(userID: Long) = "$USERS/$userID"
      fun userRoles(userID: Long) = "${user(userID)}/roles"
      fun userArticles(userID: Long) = "${user(userID)}/articles"
      fun userDrafts(userID: Long) = "${user(userID)}/drafts"

}
