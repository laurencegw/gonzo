package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest

/**
 * A policy for basic expressions between attribute values and provided values.
 */
class ExpressionPolicy(
        val leftOperand: ValueReference,
        val operatorType: OperatorType,
        val rightOperand: ValueReference
) : AccessDecider {

    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        val left = leftOperand.get(accessRequest)
        val right = rightOperand.get(accessRequest)
            return when (operatorType) {
                OperatorType.EQUAL -> left == right
                OperatorType.GREATER_THAN,
                OperatorType.GREATER_THAN_EQUAL,
                OperatorType.LESS_THAN,
                OperatorType.LESS_THAN_EQUAL -> checkComparable(left, right, operatorType)
                OperatorType.IS_IN -> {
                    @Suppress("UNCHECKED_CAST")
                    try {
                        right as Collection<Any>
                    } catch (e: Exception) {
                        throw BadExpressionException("Value of $rightOperand is not Collection")
                    }
                    right.contains(left)
                }
                OperatorType.CONTAINS ->{
                    @Suppress("UNCHECKED_CAST")
                    try {
                        left as Collection<Any>
                    } catch (e: Exception) {
                        throw BadExpressionException("Value of $leftOperand is not Collection")
                    }
                    left.contains(right)
                }
                OperatorType.CONTAINS_ANY,
                OperatorType.CONTAINS_ALL -> checkCollection(left,right, operatorType)
            }
    }

    @Suppress("UNCHECKED_CAST")
    private fun checkComparable(left: Any?, right: Any?, operatorType: OperatorType):Boolean {
        try {
            left as Comparable<Any> > right as Comparable<Any>
        } catch (e: Exception) {
            throw BadExpressionException("Values of $leftOperand and $rightOperand are not comparable")
        }
        return when(operatorType){
            OperatorType.GREATER_THAN-> left> right
            OperatorType.GREATER_THAN_EQUAL -> left >= right
            OperatorType.LESS_THAN -> left < right
            OperatorType.LESS_THAN_EQUAL -> left <= right
            else -> throw IncorrectOperator("$operatorType not recognized as a Comparison operator")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun checkCollection(left: Any?, right: Any?, operatorType: OperatorType):Boolean {
        try {
            left as Collection<Any>
            right as Collection<Any>
        } catch (e: Exception) {
            throw BadExpressionException("Values of $leftOperand and $rightOperand are not Collections")
        }
        return when(operatorType){
            OperatorType.CONTAINS_ALL-> left.containsAll(right)
            OperatorType.CONTAINS_ANY-> right.any { left.contains(it) }
            else -> throw IncorrectOperator("$operatorType not recognized as a Collection operator")
        }
    }

}

class BadExpressionException(message: String) : Exception(message)
class IncorrectOperator(message: String): Exception(message)


enum class AttributeType {
    SUBJECT,
    RESOURCE,
    ENVIRONMENT
}

enum class OperatorType {
    EQUAL,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL,
    CONTAINS,
    CONTAINS_ALL,
    CONTAINS_ANY,
    IS_IN
}

interface ValueReference {
    fun get(accessRequest: AccessRequest): Any?
}

data class PassThroughReference(val value: Any?) : ValueReference {
    override fun get(accessRequest: AccessRequest): Any? {
        return value
    }
}

data class AttributeReference(
        val type: AttributeType,
        val key: String
) : ValueReference {
    override fun get(accessRequest: AccessRequest): Any? {
        return when (type) {
            AttributeType.SUBJECT -> accessRequest.subject[key]
            AttributeType.RESOURCE -> accessRequest.resource[key]
            AttributeType.ENVIRONMENT -> accessRequest.environment[key]
        }
    }
}

class ActionReference: ValueReference{
    override fun get(accessRequest: AccessRequest): Any? {
        return accessRequest.action
    }
}

/**
 * Initializing token for an ExpressionPolicy builder.
 */
class Expression {
    fun subject(key: String) = Operator(AttributeReference(AttributeType.SUBJECT, key))

    fun action() = Operator(ActionReference())

    fun resource(key: String) = Operator(AttributeReference(AttributeType.RESOURCE, key))

    fun environment(key: String) =  Operator(AttributeReference(AttributeType.ENVIRONMENT, key))
}

class Operator internal constructor(
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

    private fun secondOperand(operatorType: OperatorType) = SecondOperand(
            leftValueReference = leftValueReference,
            operatorType = operatorType
    )
}


/**
 * Terminating token for an ExpressionPolicy builder.
 */
class SecondOperand internal constructor(
        private val leftValueReference: ValueReference,
        private val operatorType: OperatorType
) {

    fun value(value: Any) = ExpressionPolicy(
            leftOperand = leftValueReference,
            operatorType = operatorType,
            rightOperand = PassThroughReference(value)
    )

    fun resource(key: String) = expression(AttributeType.RESOURCE, key)

    fun environment(key: String) = expression(AttributeType.ENVIRONMENT, key)

    fun subject(key: String) = expression(AttributeType.SUBJECT, key)

    private fun expression(attributeType: AttributeType, key: String) = ExpressionPolicy(
            leftOperand = leftValueReference,
            operatorType = operatorType,
            rightOperand = AttributeReference(attributeType, key)
    )

}
