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

enum class Operator {
    EQUALS,
    GREATER_THAN,
    GREATER_THANT_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL
}

}enum class AttributeType {
    SUBJECT,
    RESOURCE,
    ENVIRONMENT
}

data class AttributeReference(
        private val attributeType: AttributeType,
        private val attributeName: String
) {
    internal fun get(accessRequest: AccessRequest): Any? {
        return when (attributeType) {
            AttributeType.SUBJECT -> accessRequest.subject[attributeName]
            AttributeType.RESOURCE -> accessRequest.resource[attributeName]
            AttributeType.ENVIRONMENT -> accessRequest.environment[attributeName]
        }
    }
}


class AttributeComparisonMatcher(
        private val attribute1: AttributeReference,
        private val operator: Operator,
        private val attribute2: AttributeReference
) : AccessDecider {

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        val value1 = attribute1.get(accessRequest)
        val value2 = attribute2.get(accessRequest)
        return if (operator == Operator.EQUALS) {
            value1 == value2
        } else {
            if (value1 is Comparable<T> && value2 is Comparable<T>) {
                    when(operator){
                        Operator.GREATER_THANT_EQUAL -> value1.compareTo(value2) >=0
                    }
            }
            else {

            }
        }

    }
}

class AllOf(val deciders: List<AccessDecider> = mutableListOf()) : AccessDecider {

    constructor(vararg deciders: AccessDecider) : this(deciders.toList())

    constructor(builder: AllOf.() -> Unit) : this() {
        this.builder()
    }

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}



