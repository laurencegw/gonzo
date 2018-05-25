package com.binarymonks.gonzo.core.blog

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.blog"
])
@EnableJpaRepositories(basePackages = [
    "com.binarymonks.gonzo.core.blog.persistence"
])
@EntityScan(basePackages = [
    "com.binarymonks.gonzo.core.blog.persistence"
])
class BlogConfig