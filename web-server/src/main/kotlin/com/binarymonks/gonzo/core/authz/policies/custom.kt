package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest

class CustomPolicy(
        private val customRule: AccessRequest.()->Boolean
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return customRule(accessRequest)
    }
}