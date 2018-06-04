package com.binarymonks.gonzo.web

import com.binarymonks.gonzo.core.GonzoCoreConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(
        GonzoCoreConfig::class,
        GonzoDataConfig::class,
        GonzoSecurityConfig::class
)
class GonzoApplication

fun main(args: Array<String>) {
    SpringApplication.run(GonzoApplication::class.java, *args)
}
