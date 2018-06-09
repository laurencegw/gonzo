package com.binarymonks.gonzo.core

import com.binarymonks.gonzo.core.authz.AuthorizationConfig
import com.binarymonks.gonzo.core.blog.BlogConfig
import com.binarymonks.gonzo.core.users.UsersConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
        BlogConfig::class,
        UsersConfig::class,
        AuthorizationConfig::class
)
class GonzoCoreConfig