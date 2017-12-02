package com.binarymonks.me.api

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class MeTests {

    @ParameterizedTest
    @MethodSource("me")
    fun createAndGetBlogEntry(me: Me) {

    }


    fun me(){

    }


}
