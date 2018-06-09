package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.users.api.Role
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class UsersAuthServiceTest {

    fun createUserScenarios(): List<Arguments> {
        return listOf(
                Arguments.of()
        )
    }

    @ParameterizedTest
    @MethodSource("createUserScenarios")
    fun createUserAuth(userRole: Role, authorized: Boolean){

    }
}