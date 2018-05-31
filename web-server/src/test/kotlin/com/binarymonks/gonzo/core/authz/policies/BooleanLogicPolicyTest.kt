package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.accessRequest
import com.binarymonks.gonzo.core.authz.api.AccessRequest
import org.junit.Test
import org.junit.jupiter.api.Assertions


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