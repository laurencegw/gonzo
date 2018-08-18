package com.binarymonks.gonzo.core.common

import javax.validation.Validation
import kotlin.reflect.full.declaredMemberProperties

class Types{
    companion object {
        const val USER = "USER"
        const val USER_ROLES = "USER_ROLES"
        const val ARTICLE = "ARTICLE"
        const val ARTICLE_DRAFT = "ARTICLE_DRAFT"
    }
}

open class Resource(val type: String) {

    companion object {
        val validator = Validation.buildDefaultValidatorFactory().validator!!
    }

    open fun attributes(): Map<String, Any?> {
        val map: MutableMap<String, Any?> = mutableMapOf(Pair("type", type))
        map.putAll(allProps())
        return map
    }

    protected fun hasProperty(name: String):Boolean{
        val clazz = this::class
        for (prop in clazz.declaredMemberProperties){
            if (prop.name == name){
                return true
            }
        }
        return false
    }

    private fun allProps(): Map<String,Any?>{
        val atts: MutableMap<String, Any?> = mutableMapOf()
        for (prop in this::class.declaredMemberProperties){
            val value = prop.getter.call(this)
            atts.put(prop.name, value)
        }
        return atts
    }

    protected fun getProperty(name:String): Any?{
        val clazz = this::class
        @Suppress("UNCHECKED_CAST")
        return clazz.declaredMemberProperties.first { it.name == name }.getter.call(this)
    }

    fun validate(){
        val violations = validator.validate(this)
        val messages = violations.map {
            ValidationMessage(it.propertyPath.toString(), it.message)
        }.toMutableList()
        if (messages.isNotEmpty()) {
            throw ValidationException(validationMessages= ValidationMessages(messages))
        }
    }
}