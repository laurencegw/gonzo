package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.core.blog.BlogConfig
import com.binarymonks.gonzo.core.users.UsersConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(
        BlogConfig::class,
        UsersConfig::class,
        GonzoDataConfig::class,
        GonzoSecurityConfig::class
)
class GonzoApplication

fun main(args: Array<String>) {
    SpringApplication.run(GonzoApplication::class.java, *args)
}
