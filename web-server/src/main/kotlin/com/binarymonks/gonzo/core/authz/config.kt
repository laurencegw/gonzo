package com.binarymonks.gonzo.core.authz

import com.binarymonks.gonzo.core.authz.policies.*
import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import com.binarymonks.gonzo.core.common.Actions
import com.binarymonks.gonzo.core.common.Types
import com.binarymonks.gonzo.core.users.api.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthorizationConfig {

    @Bean
    fun accessDecisionService(): AccessDecisionService {
        return AccessDecisionService(
                listOf(
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
                        }
                )
        )
    }
}