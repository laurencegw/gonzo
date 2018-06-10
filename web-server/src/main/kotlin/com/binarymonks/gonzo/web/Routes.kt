package com.binarymonks.gonzo.web

object Routes {
      const val API = "/api"
      const val LOGIN = "/login"

      const val BLOGS = "$API/blogs"

      const val USERS = "$API/users"
      const val ME = "$USERS/me"
      fun user(userID: Long) = "$USERS/$userID"
      fun userRoles(userID: Long) = "${user(userID)}/roles"

}
