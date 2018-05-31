package com.binarymonks.gonzo.core.authz

import com.binarymonks.gonzo.core.authz.policies.CustomPolicy
import com.binarymonks.gonzo.core.authz.service.AccessDecisionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthorizationConfig {

    @Bean
    fun accessDecisionService(): AccessDecisionService {
        return AccessDecisionService(
                listOf(
                        // Admins can create Blogs
                        CustomPolicy {
                            listOf(
                                    (subject["role"] in listOf("ADMIN")),
                                    action == "CREATE",
                                    (resource["type"] in listOf("BLOG"))
                            ).all { it }
                        },
                        // Anyone can read Blogs
                        CustomPolicy {
                            listOf(
                                    action == "READ",
                                    (resource["type"] in listOf("BLOG"))
                            ).all { it }
                        },
                        // Only owners of the Blog can edit the blog
                        CustomPolicy {
                            listOf(
                                    action == "EDIT",
                                    (resource["type"] in listOf("BLOG")),
                                    (resource["ownerID"] == subject["id"])
                            ).all { it }
                        }
                )
        )
    }
}