package com.binarymonks.gonzo.core.authz.api

interface AccessDecider {
    fun checkAuthorized(accessRequest: AccessRequest): Boolean
}


data class AccessRequest(
        val subject: Map<String, Any?>,
        val action: String,
        val resource: Map<String, Any?>,
        val environment: Map<String, Any?> = emptyMap()
)