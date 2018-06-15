package com.binarymonks.gonzo.core.common

import kotlin.reflect.full.declaredMemberProperties

class Types{
    companion object {
        const val USER = "USER"
        const val USER_ROLES = "USER_ROLES"
        const val BLOG = "BLOG"
        const val BLOG_DRAFT = "BLOG_DRAFT"
    }
}

open class Resource(val type: String) {

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
}