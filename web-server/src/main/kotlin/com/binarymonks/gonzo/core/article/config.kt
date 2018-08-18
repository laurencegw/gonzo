package com.binarymonks.gonzo.core.article

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.article"
])
@EnableJpaRepositories(basePackages = [
    "com.binarymonks.gonzo.core.article.persistence"
])
@EntityScan(basePackages = [
    "com.binarymonks.gonzo.core.article.persistence"
])
class ArticleConfig