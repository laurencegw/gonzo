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
                        allOf {
                            subject("role").equalTo().value(Role.ADMIN)
                            anyOf {
                                allOf {
                                    action().isIn().value(listOf(Actions.CREATE))
                                    resource("type").isIn().value(listOf(Types.USER))
                                }
                            }
                        }
                )
        )
    }
}