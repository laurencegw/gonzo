package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest

class UserAttributeMatcher(
        val key: String,
        val value: Any
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return accessRequest.subject.containsKey(key) && accessRequest.subject[key] == value
    }
}

class ResourceAttributeMatcher(
        val key: String,
        val value: Any
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return accessRequest.resource.containsKey(key) && accessRequest.resource[key] == value
    }
}

class EnvironmentAttributeMatcher(
        val key: String,
        val value: Any
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return accessRequest.environment.containsKey(key) && accessRequest.environment[key] == value
    }
}

class ActionMatcher(
        val action: String
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return action.toLowerCase() == accessRequest.action.toLowerCase()
    }
}








