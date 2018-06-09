package com.binarymonks.gonzo.core.test

import com.binarymonks.gonzo.core.GonzoCoreConfig
import com.binarymonks.gonzo.web.GonzoDataConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.test"
])
@Import(
        GonzoCoreConfig::class,
        GonzoDataConfig::class
)
class GonzoTestConfig