package com.binarymonks.me

import com.binarymonks.me.core.blog.service.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = arrayOf(
        "com.binarymonks.me.core.blog.persistence"
))
@EnableJpaRepositories(basePackages = arrayOf(
        "com.binarymonks.me.core.blog.persistence"
))
@PropertySource("test_db.properties")
@EnableTransactionManagement
class TestConfig {

    @Autowired
    lateinit var env: Environment

    @Bean
    fun blogService(): BlogService = BlogService()

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