package com.binarymonks.me.web

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
    "com.binarymonks.me.core",
    "com.binarymonks.me.web"
])
class GonzoApplication

fun main(args: Array<String>) {
    SpringApplication.run(GonzoApplication::class.java, *args)
}
