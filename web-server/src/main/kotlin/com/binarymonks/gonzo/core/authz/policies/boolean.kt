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

class AllOf(policies: MutableList<AccessDecider>) : AccessDecider, CollectionBasedPolicy(policies) {

    constructor(builder: CollectionBasedPolicy.()->Unit):this(mutableListOf()){
        this.builder()
    }

    constructor(vararg policies: AccessDecider) : this(policies.toMutableList())

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return if (policies.isEmpty()) {
            false
        } else {
            policies.all { it.checkAuthorized(accessRequest) }
        }
    }

}

fun allOf(builder: CollectionBasedPolicy.()->Unit) = AllOf(builder)

class AnyOf(policies: MutableList<AccessDecider>) : AccessDecider, CollectionBasedPolicy(policies) {

    constructor(builder: CollectionBasedPolicy.()->Unit):this(mutableListOf()){
        this.builder()
    }

    constructor(vararg policies: AccessDecider) : this(policies.toMutableList())

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return if (policies.isEmpty()) {
            false
        } else {
            policies.any { it.checkAuthorized(accessRequest) }
        }
    }
}

fun anyOf(builder: CollectionBasedPolicy.()->Unit) = AnyOf(builder)


class Not(val policy: AccessDecider) : AccessDecider {

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return !policy.checkAuthorized(accessRequest)
    }

}

open class CollectionBasedPolicy(val policies: MutableList<AccessDecider>) {

    fun add(policy: AccessDecider){
        policies.add(policy)
    }
}

fun CollectionBasedPolicy.subject(key: String) = PolicyCollectionOperator(this, AttributeReference(AttributeType.SUBJECT, key))

fun CollectionBasedPolicy.action() = PolicyCollectionOperator(this, ActionReference())

fun CollectionBasedPolicy.resource(key: String) = PolicyCollectionOperator(this, AttributeReference(AttributeType.RESOURCE, key))

fun CollectionBasedPolicy.environment(key: String) = PolicyCollectionOperator(this, AttributeReference(AttributeType.ENVIRONMENT, key))

fun CollectionBasedPolicy.anyOf(builder: CollectionBasedPolicy.()->Unit){
    val anyOf = AnyOf()
    anyOf.builder()
    this.add(anyOf)
}

fun CollectionBasedPolicy.allOf(builder: CollectionBasedPolicy.()->Unit){
    val allOf = AllOf()
    allOf.builder()
    this.add(allOf)
}

fun CollectionBasedPolicy.notAllOf(builder: CollectionBasedPolicy.()->Unit){
    val allOf = AllOf()
    allOf.builder()
    this.add(Not(allOf))
}

fun CollectionBasedPolicy.notAnyOf(builder: CollectionBasedPolicy.()->Unit){
    val anyOf = AnyOf()
    anyOf.builder()
    this.add(Not(anyOf))
}

class PolicyCollectionOperator internal constructor(
        private val collectionBasedPolicy: CollectionBasedPolicy,
        private val leftValueReference: ValueReference
) {

    fun equalTo() = secondOperand(OperatorType.EQUAL)

    fun greaterThan() = secondOperand(OperatorType.GREATER_THAN)

    fun greaterThanEqual() = secondOperand(OperatorType.GREATER_THAN_EQUAL)

    fun lessThan() = secondOperand(OperatorType.LESS_THAN)

    fun lessThanEqual() = secondOperand(OperatorType.LESS_THAN_EQUAL)

    fun contains() = secondOperand(OperatorType.CONTAINS)

    fun containsAll() = secondOperand(OperatorType.CONTAINS_ALL)

    fun containsAny() = secondOperand(OperatorType.CONTAINS_ANY)

    fun isIn() = secondOperand(OperatorType.IS_IN)

    private fun secondOperand(operatorType: OperatorType) = PolicyCollectionSecondOperand(
            collectionBasedPolicy = collectionBasedPolicy,
            leftValueReference = leftValueReference,
            operatorType = operatorType
    )
}

class PolicyCollectionSecondOperand internal constructor(
        private val collectionBasedPolicy: CollectionBasedPolicy,
        private val leftValueReference: ValueReference,
        private val operatorType: OperatorType
) {

    fun value(value: Any) {
        val policy = ExpressionPolicy(
                leftOperand = leftValueReference,
                operatorType = operatorType,
                rightOperand = PassThroughReference(value)
        )
        collectionBasedPolicy.add(policy)
    }

    fun resource(key: String) = expression(AttributeType.RESOURCE, key)

    fun environment(key: String) = expression(AttributeType.ENVIRONMENT, key)

    fun subject(key: String) = expression(AttributeType.SUBJECT, key)

    private fun expression(attributeType: AttributeType, key: String) {
        val policy = ExpressionPolicy(
                leftOperand = leftValueReference,
                operatorType = operatorType,
                rightOperand = AttributeReference(attributeType, key)
        )
        collectionBasedPolicy.add(policy)
    }

}







