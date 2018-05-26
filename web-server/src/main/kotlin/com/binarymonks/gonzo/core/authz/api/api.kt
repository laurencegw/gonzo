package com.binarymonks.gonzo.core.authz.api

interface AccessDecider{
    fun checkAuthorized(accessRequest:AccessRequest)
}

class NotAuthorized:Exception()

data class AccessRequest(
        val subject: Map<String, Any>,
        val action: Map<String, Any>,
        val resource: Map<String, Any>,
        val environment: Map<String,Any> = emptyMap()
)