package com.binarymonks.gonzo.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@PropertySource("classpath:db.properties")
class DataConfig {

    @Autowired
    lateinit var env: Environment

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName")!!)
        dataSource.url = env.getProperty("jdbc.url")
        dataSource.username = env.getProperty("jdbc.user")
        dataSource.password = env.getProperty("jdbc.pass")

        return dataSource
    }
}