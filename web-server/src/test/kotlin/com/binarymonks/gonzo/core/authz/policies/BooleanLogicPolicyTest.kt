package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.accessRequest
import org.junit.Test
import org.junit.jupiter.api.Assertions


class BasicPolicyTest {

    @Test
    fun userAttributePolicy() {
        val accessRequest = accessRequest().copy(
                subject = mapOf(Pair("id", 20))
        )
        val matchingPolicy = SubjectAttributePolicy("id", 20)
        val sameKeyDifferentValuePolicy = SubjectAttributePolicy("id", 21)
        val sameValueDifferentKeyPolicy = SubjectAttributePolicy("age", 21)

        Assertions.assertTrue(matchingPolicy.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameKeyDifferentValuePolicy.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameValueDifferentKeyPolicy.checkAuthorized(accessRequest))
    }

    @Test
    fun resourceAttributePolicy() {
        val accessRequest = accessRequest().copy(
                resource = mapOf(Pair("id", 20))
        )

        val matchingPolicy = ResourceAttributePolicy("id", 20)
        val sameKeyDifferentValuePolicy = ResourceAttributePolicy("id", 21)
        val sameValueDifferentKeyPolicy = ResourceAttributePolicy("age", 21)

        Assertions.assertTrue(matchingPolicy.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameKeyDifferentValuePolicy.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameValueDifferentKeyPolicy.checkAuthorized(accessRequest))
    }

    @Test
    fun environmentAttributePolicy() {
        val accessRequest = accessRequest().copy(
                environment = mapOf(Pair("id", 20))
        )

        val matchingPolicy = EnvironmentAttributePolicy("id", 20)
        val sameKeyDifferentValuePolicy = EnvironmentAttributePolicy("id", 21)
        val sameValueDifferentKeyPolicy = EnvironmentAttributePolicy("age", 21)

        Assertions.assertTrue(matchingPolicy.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameKeyDifferentValuePolicy.checkAuthorized(accessRequest))
        Assertions.assertFalse(sameValueDifferentKeyPolicy.checkAuthorized(accessRequest))
    }

    @Test
    fun actionPolicy() {
        val accessRequest = accessRequest().copy(
                action = "READ"
        )
        val accessRequestMixedCase = accessRequest().copy(
                action = "rEAd"
        )

        Assertions.assertTrue(ActionPolicy("read").checkAuthorized(accessRequest))
        Assertions.assertTrue(ActionPolicy("read").checkAuthorized(accessRequestMixedCase))
        Assertions.assertFalse(ActionPolicy("write").checkAuthorized(accessRequestMixedCase))
    }

}

val accessRequest = accessRequest().copy(
        action = "READ"
)
val willAuthorizePolicy = ActionPolicy("READ")
val willNotAuthorizePolicy = ActionPolicy("WRITE")

class AllOfTest {

    @Test
    fun checkAuthorized_emptyPolicies() {
        Assertions.assertFalse(AllOf().checkAuthorized(accessRequest))
    }

    @Test
    fun checkAuthorized_onlySomeAuthorized() {
        Assertions.assertFalse(
                AllOf(
                        willAuthorizePolicy,
                        willAuthorizePolicy,
                        willNotAuthorizePolicy,
                        willAuthorizePolicy
                ).checkAuthorized(accessRequest)
        )
    }

    @Test
    fun checkAuthorized_allAuthorized() {
        Assertions.assertTrue(
                AllOf(
                        willAuthorizePolicy,
                        willAuthorizePolicy,
                        willAuthorizePolicy
                ).checkAuthorized(accessRequest)
        )
    }

    @Test
    fun checkAuthorized_NoneAuthorized() {
        Assertions.assertFalse(
                AllOf(
                        willNotAuthorizePolicy,
                        willNotAuthorizePolicy,
                        willNotAuthorizePolicy
                ).checkAuthorized(accessRequest)
        )
    }
}

class AnyOfTest {

    @Test
    fun checkAuthorized_emptyPolicies() {
        Assertions.assertFalse(AnyOf().checkAuthorized(accessRequest))
    }

    @Test
    fun checkAuthorized_onlySomeAuthorized() {
        Assertions.assertTrue(
                AnyOf(
                        willAuthorizePolicy,
                        willAuthorizePolicy,
                        willNotAuthorizePolicy,
                        willAuthorizePolicy
                ).checkAuthorized(accessRequest)
        )
    }

    @Test
    fun checkAuthorized_allAuthorized() {
        Assertions.assertTrue(
                AnyOf(
                        willAuthorizePolicy,
                        willAuthorizePolicy,
                        willAuthorizePolicy
                ).checkAuthorized(accessRequest)
        )
    }

    @Test
    fun checkAuthorized_NoneAuthorized() {
        Assertions.assertFalse(
                AnyOf(
                        willNotAuthorizePolicy,
                        willNotAuthorizePolicy,
                        willNotAuthorizePolicy
                ).checkAuthorized(accessRequest)
        )
    }
}
