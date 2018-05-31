package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest
import kotlin.reflect.KClass

enum class AttributeType{
    SUBJECT,
    RESOURCE,
    ENVIRONMENT
}

enum class ComparisonOperator{
    EQUAL,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL
}

data class AttributeReference<T:Comparable<T>>(
        val type: AttributeType,
        val key: String,
        val valueType: KClass<T>
):ValueReference<T>{
    override fun get(accessRequest: AccessRequest): T{
        val value =  when(type){
            AttributeType.SUBJECT -> accessRequest.subject[key]
            AttributeType.RESOURCE -> accessRequest.subject[key]
            AttributeType.ENVIRONMENT -> accessRequest.subject[key]
        }
        if(valueType.isInstance(value)){
            return value as T
        }
        else{
            throw ClassCastException("The attribute value is not of expected type $valueType")
        }
    }
}

interface ValueReference<T:Comparable<T>>{
    fun get(accessRequest: AccessRequest): T
}

class Expression<T:Comparable<T>>(
        val leftOperand: ValueReference<T>,
        val operator: ComparisonOperator,
        val rightOperand: ValueReference<T>
): AccessDecider {
    
    
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        val left = leftOperand.get(accessRequest)
        val right = rightOperand.get(accessRequest)
        return when(operator){
            ComparisonOperator.EQUAL -> left == right
            ComparisonOperator.GREATER_THAN -> left > right
            ComparisonOperator.GREATER_THAN_EQUAL -> left >= right
            ComparisonOperator.LESS_THAN -> left < right
            ComparisonOperator.LESS_THAN_EQUAL -> left <= right
        }
    }

}

class FirstOperand{

    fun subject(key: String): Operator{
        return Operator()
    }
}

class Operator{


    fun greaterThan(): SecondOperand{
        return SecondOperand()
    }
}

class SecondOperand{

    fun <T: Comparable<T>> value(value: T){

    }

    fun <T: Comparable<T>> resource(key:String, type: KClass<T>){

    }
}

fun main(args: Array<String>) {
    FirstOperand().subject("age").greaterThan().value(34)
    FirstOperand().subject("age").greaterThan().resource("ageLimit", Int::class)
}