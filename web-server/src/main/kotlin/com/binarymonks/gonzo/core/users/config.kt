package com.binarymonks.gonzo.core.users

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.users"
])
@EnableJpaRepositories(basePackages = [
    "com.binarymonks.gonzo.core.users.persistence"
])
@EntityScan(basePackages = [
    "com.binarymonks.gonzo.core.users.persistence"
])
@PropertySource("user.properties")
class UsersConfig