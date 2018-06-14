package com.binarymonks.gonzo.core.common

import kotlin.reflect.full.declaredMemberProperties

class Types{
    companion object {
        const val USER = "USER"
        const val USER_ROLES = "USER_ROLES"
        const val BLOG = "BLOG"
    }
}

open class Resource(val type: String) {

    fun attributes(): Map<String, Any?> {
        val map: MutableMap<String, Any?> = mutableMapOf(Pair("type", type))
        if(hasProperty("id")){
            map["id"] = getProperty("id")
        }
        return map
    }

    private fun hasProperty(name: String):Boolean{
        val clazz = this::class
        for (prop in clazz.declaredMemberProperties){
            if (prop.name == name){
                return true
            }
        }
        return false
    }

    private fun getProperty(name:String): Any?{
        val clazz = this::class
        @Suppress("UNCHECKED_CAST")
        return clazz.declaredMemberProperties.first { it.name == name }.getter.call(this)
    }
}