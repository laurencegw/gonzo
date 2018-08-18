package com.binarymonks.gonzo.core.test.harness

import com.binarymonks.gonzo.core.article.persistence.ArticleRepo
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.api.UserNew
import com.binarymonks.gonzo.core.users.api.UserRoleUpdate
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import com.binarymonks.gonzo.core.users.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class TestDataManager {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var articleRepo: ArticleRepo

    fun forceCreateUser(newUser:UserNew, role: Role = Role.READER): User {
        val user = userService.createUser(newUser)
        userService.setUserRole(UserRoleUpdate(user.id, role))
        return user
    }

    fun clearData() {
        articleRepo.deleteAll()
        userRepo.deleteAll()
    }

}