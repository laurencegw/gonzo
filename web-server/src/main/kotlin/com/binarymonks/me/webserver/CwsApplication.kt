package com.binarymonks.me.webserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CwsApplication

fun main(args: Array<String>) {
    SpringApplication.run(CwsApplication::class.java, *args)
}
