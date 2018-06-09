package com.binarymonks.gonzo.core.authz

import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthorizationConfig {

    @Bean
    fun accessDecisionService(): AccessDecisionService {
        return AccessDecisionService(
                listOf()
        )
    }
}