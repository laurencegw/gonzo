package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.authz.service.AuthorizedService
import com.binarymonks.gonzo.core.common.Actions
import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.users.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersAuthService(
        @Autowired signInService: SignInService ,
        @Autowired accessDecisionService: AccessDecisionService
): UsersAuth, AuthorizedService(signInService, accessDecisionService) {

    @Autowired
    lateinit var userService: UserService

    override fun createUser(credentials: Credentials, user: UserNew): User {
        checkAuth(credentials,Actions.CREATE,user.attributes())
        return userService.createUser(user)
    }

    override fun updateUser(credentials: Credentials, user: UserUpdate): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserByEmail(credentials: Credentials, email: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestPasswordResetEmail(credentials: Credentials, email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetPassword(credentials: Credentials, passwordReset: PasswordReset) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserRole(credentials: Credentials, userID: Long, role: Role) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}