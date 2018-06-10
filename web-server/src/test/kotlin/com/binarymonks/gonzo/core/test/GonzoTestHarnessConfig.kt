package com.binarymonks.gonzo.core.test

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
    "com.binarymonks.gonzo.core.test.harness"
])
class GonzoTestHarnessConfig