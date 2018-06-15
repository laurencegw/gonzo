package com.binarymonks.gonzo.web

object Routes {
      const val API = "/api"
      const val LOGIN = "/login"

      const val BLOGS = "$API/blogs"
      fun blogEntry(blogID: Long) = "$BLOGS/$blogID"
      fun publishBlog(blogID: Long) = "${blogEntry(blogID)}/publish"
      fun blogDraft(blogID: Long) = "${blogEntry(blogID)}/draft"

      const val USERS = "$API/users"
      const val ME = "$USERS/me"
      fun user(userID: Long) = "$USERS/$userID"
      fun userRoles(userID: Long) = "${user(userID)}/roles"
      fun userBlogs(userID: Long) = "${user(userID)}/blogs"
      fun userDrafts(userID: Long) = "${user(userID)}/drafts"

}
