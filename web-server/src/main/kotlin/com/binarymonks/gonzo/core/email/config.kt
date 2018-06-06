package com.binarymonks.gonzo.core.email

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.email"
])
class EmailConfig