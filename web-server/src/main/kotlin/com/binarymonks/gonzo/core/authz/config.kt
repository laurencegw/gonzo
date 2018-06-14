package com.binarymonks.gonzo.core.authz

import com.binarymonks.gonzo.core.authz.policies.*
import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.common.Actions
import com.binarymonks.gonzo.core.common.Types
import com.binarymonks.gonzo.core.users.api.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


val USER_POLICIES = listOf(
        // ADMIN ROLE PERMISSIONS
        allOf {
            subject("role").equalTo().value(Role.ADMIN)
            anyOf {
                allOf {
                    action().equalTo().value(Actions.CREATE)
                    resource("type").equalTo().value(Types.USER)
                }
                allOf {
                    action().equalTo().value(Actions.MODIFY)
                    resource("type").equalTo().value(Types.USER_ROLES)
                }
            }
        },
        // UPDATE USER DATA
        allOf {
            resource("type").equalTo().value(Types.USER)
            action().equalTo().value(Actions.MODIFY)
            subject("id").equalTo().resource("id")
        }
)

val BLOG_POLICIES = listOf(
        // CREATE OWN
        allOf {
            resource("type").equalTo().value(Types.BLOG)
            subject("role").isIn().value(listOf(Role.ADMIN, Role.AUTHOR))
            subject("id").equalTo().resource("authorID")
            action().equalTo().value(Actions.CREATE)
        },
        // Modify Own
        allOf {
            resource("type").equalTo().value(Types.BLOG)
            subject("id").equalTo().resource("authorID")
            action().equalTo().value(Actions.MODIFY)
        }
)


@Configuration
class AuthorizationConfig {

    @Bean
    fun accessDecisionService(): AccessDecisionService {
        return AccessDecisionService(
            USER_POLICIES + BLOG_POLICIES
        )
    }
}