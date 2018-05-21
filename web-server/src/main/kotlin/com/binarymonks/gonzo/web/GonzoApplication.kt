package com.binarymonks.gonzo.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core",
    "com.binarymonks.gonzo.web"
])
@EnableJpaRepositories(basePackages = [
    "com.binarymonks.gonzo.core.blog.persistence"
])
@EntityScan(basePackages = [
    "com.binarymonks.gonzo.core.blog.persistence"
])
class GonzoApplication

fun main(args: Array<String>) {
    SpringApplication.run(GonzoApplication::class.java, *args)
}
