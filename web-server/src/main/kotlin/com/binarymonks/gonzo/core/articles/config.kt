package com.binarymonks.gonzo.core.articles

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.articles"
])
@EnableJpaRepositories(basePackages = [
    "com.binarymonks.gonzo.core.articles.persistence"
])
@EntityScan(basePackages = [
    "com.binarymonks.gonzo.core.articles.persistence"
])
class ArticleConfig