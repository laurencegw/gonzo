package com.binarymonks.gonzo.core.authz.service

import com.binarymonks.gonzo.core.authz.api.AccessRequest
import com.binarymonks.gonzo.core.common.AnonymousCredentials
import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.common.TokenCredentials
import com.binarymonks.gonzo.core.users.api.Role
import com.binarymonks.gonzo.core.users.service.SignInService


abstract class AuthorizedService(
        val signInService: SignInService,
        val accessDecisionService: AccessDecisionService
) {

    private fun getSubjectAttributes(credentials: Credentials): Map<String, Any?> {
        return when (credentials) {
            is TokenCredentials -> {
                val user = signInService.getUserFromToken(credentials.token)
                mapOf(
                        Pair("id", user.id),
                        Pair("role", user.role)
                )
            }
            is AnonymousCredentials -> {
                mapOf(Pair("role", Role.READER))
            }
        }
    }

    fun checkAuth(credentials: Credentials, action: String, resource: Map<String, Any?>) {
        if (!accessDecisionService.checkAuthorized(AccessRequest(
                        subject = getSubjectAttributes(credentials),
                        action = action,
                        resource = resource
                ))) {
            throw NotAuthorized()
        }
    }

}