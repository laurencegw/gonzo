package com.binarymonks.gonzo.core.authz.service

import com.binarymonks.gonzo.core.authz.api.AccessRequest
import org.junit.Test
import org.junit.jupiter.api.Assertions

class AccessDecisionServiceTest {

    private val accessRequest1 = AccessRequest(
            subject = mapOf(Pair("id", 1)),
            action = "READ",
            resource = mapOf()
    )
    private val accessRequest2 = AccessRequest(
            subject = mapOf(Pair("id", 2)),
            action = "WRITE",
            resource = mapOf()
    )

    @Test
    fun checkAccess_NoDecidersGrantAccess(){
        val decisionService = AccessDecisionService(
                listOf(
                        StubAccessDecider(accessRequest2),
                        StubAccessDecider(accessRequest2)
                )
        )
        Assertions.assertFalse(decisionService.checkAuthorized(accessRequest1))
    }

    @Test
    fun checkAccess_ADecidersGrantAccess(){
        val decisionService = AccessDecisionService(
                listOf(
                        StubAccessDecider(accessRequest2),
                        StubAccessDecider(accessRequest1),
                        StubAccessDecider(accessRequest2)
                )
        )
        Assertions.assertTrue(decisionService.checkAuthorized(accessRequest1))
    }

}

