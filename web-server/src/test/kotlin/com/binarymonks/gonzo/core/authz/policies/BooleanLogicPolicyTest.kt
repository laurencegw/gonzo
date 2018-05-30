package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.accessRequest
import com.binarymonks.gonzo.core.authz.api.AccessRequest
import org.junit.Test
import org.junit.jupiter.api.Assertions

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


class BasicMatcherTest {

    @Test
    fun userAttributeMatcher() {
        val accessRequest = accessRequest().copy(
                subject = mapOf(Pair("id", 20))
        )
        val matchingMatcher = UserAttributeMatcher("id", 20)
        val sameKeyDifferentValueMatcher = UserAttributeMatcher("id", 21)
        val sameValueDifferentKeyMatcher = UserAttributeMatcher("age", 21)

        Assertions.assertTrue(matchingMatcher.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameKeyDifferentValueMatcher.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameValueDifferentKeyMatcher.checkAuthorized(accessRequest))
    }

    @Test
    fun resourceAttributeMatcher() {
        val accessRequest = accessRequest().copy(
                resource = mapOf(Pair("id", 20))
        )

        val matchingMatcher = ResourceAttributeMatcher("id", 20)
        val sameKeyDifferentValueMatcher = ResourceAttributeMatcher("id", 21)
        val sameValueDifferentKeyMatcher = ResourceAttributeMatcher("age", 21)

        Assertions.assertTrue(matchingMatcher.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameKeyDifferentValueMatcher.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameValueDifferentKeyMatcher.checkAuthorized(accessRequest))
    }

    @Test
    fun environmentAttributeMatcher() {
        val accessRequest = accessRequest().copy(
                environment = mapOf(Pair("id", 20))
        )

        val matchingMatcher = EnvironmentAttributeMatcher("id", 20)
        val sameKeyDifferentValueMatcher = EnvironmentAttributeMatcher("id", 21)
        val sameValueDifferentKeyMatcher = EnvironmentAttributeMatcher("age", 21)

        Assertions.assertTrue(matchingMatcher.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameKeyDifferentValueMatcher.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameValueDifferentKeyMatcher.checkAuthorized(accessRequest))
    }

    @Test
    fun attributeComparisonMatcher(){

    }

    @Test
    fun actionMatcher() {
        val accessRequest = accessRequest().copy(
                action = "READ"
        )
        val accessRequestMixedCase = accessRequest().copy(
                action = "rEAd"
        )

        Assertions.assertTrue(ActionMatcher("read").checkAuthorized(accessRequest))
        Assertions.assertTrue(ActionMatcher("read").checkAuthorized(accessRequestMixedCase))
        Assertions.assertFalse(ActionMatcher("write").checkAuthorized(accessRequestMixedCase))
    }

}

class AllOfMatcherTest {

    fun emptyDoesNotMatchAnything() {
        val allOfMatcher = AllOf()
        Assertions.assertFalse(allOfMatcher.checkAuthorized(accessRequest1))
    }

    fun noAccessWhenOnlySomeMach() {
//        val accessRequest = AccessRequest(
//                subject = mapOf(Pair("role", "Admin")),
//                action = "write",
//                resource = mapOf(Pair("type", "Blog"))
//        )
//
//        val allOfMatcher = AllOf{
//            subjectHasAttribute("role", "Admin")
//            actionIs("Read")
//            resourceHasAttribute("type", "Blog")
//        }
//        Assertions.assertFalse(allOfMatcher.checkAuthorized(accessRequest1))
    }
}