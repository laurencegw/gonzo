package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest

/**
 * Checks if a Subject has a particular attribute value.
 */
class SubjectAttributePolicy(
        val key: String,
        val value: Any
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return accessRequest.subject.containsKey(key) && accessRequest.subject[key] == value
    }
}

/**
 * Checks if the Resource has a particular attribute value.
 */
class ResourceAttributePolicy(
        val key: String,
        val value: Any
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return accessRequest.resource.containsKey(key) && accessRequest.resource[key] == value
    }
}

/**
 * Checks if the Environment has a particular attribute value.
 */
class EnvironmentAttributePolicy(
        val key: String,
        val value: Any
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return accessRequest.environment.containsKey(key) && accessRequest.environment[key] == value
    }
}

/**
 * Checks if the Action matches a particular value.
 */
class ActionPolicy(
        val action: String
) : AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return action.toLowerCase() == accessRequest.action.toLowerCase()
    }
}

class AllOf(val policies: List<AccessDecider>) : AccessDecider {

    constructor(vararg policies: AccessDecider) : this(policies.toList())

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return if (policies.isEmpty()) {
            false
        } else {
            policies.all { it.checkAuthorized(accessRequest) }
        }
    }

}

class AnyOf(val policies: List<AccessDecider>) : AccessDecider {

    constructor(vararg policies: AccessDecider) : this(policies.toList())

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return if (policies.isEmpty()) {
            false
        } else {
            policies.any { it.checkAuthorized(accessRequest) }
        }
    }
}








