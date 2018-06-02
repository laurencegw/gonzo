package com.binarymonks.gonzo.core.authz.policies

import com.binarymonks.gonzo.accessRequest
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.time.LocalDateTime


class ExpressionPolicyComparisonTest {

    @Test
    fun comparisonOperators() {
        val request17 = accessRequest().copy(
                subject = mapOf(Pair("age", 17))
        )
        val request18 = accessRequest().copy(
                subject = mapOf(Pair("age", 18))
        )
        val request19 = accessRequest().copy(
                subject = mapOf(Pair("age", 19))
        )

        val greaterThan18 = Expression().subject("age").greaterThan().value(18)
        Assertions.assertFalse(greaterThan18.checkAuthorized(request17))
        Assertions.assertFalse(greaterThan18.checkAuthorized(request18))
        Assertions.assertTrue(greaterThan18.checkAuthorized(request19))

        val greaterThanEqual18 = Expression().subject("age").greaterThanEqual().value(18)
        Assertions.assertFalse(greaterThanEqual18.checkAuthorized(request17))
        Assertions.assertTrue(greaterThanEqual18.checkAuthorized(request18))
        Assertions.assertTrue(greaterThanEqual18.checkAuthorized(request19))

        val equal18 = Expression().subject("age").equalTo().value(18)
        Assertions.assertFalse(equal18.checkAuthorized(request17))
        Assertions.assertTrue(equal18.checkAuthorized(request18))
        Assertions.assertFalse(equal18.checkAuthorized(request19))

        val lessThan18 = Expression().subject("age").lessThan().value(18)
        Assertions.assertTrue(lessThan18.checkAuthorized(request17))
        Assertions.assertFalse(lessThan18.checkAuthorized(request18))
        Assertions.assertFalse(lessThan18.checkAuthorized(request19))

        val lessThanEqual18 = Expression().subject("age").lessThanEqual().value(18)
        Assertions.assertTrue(lessThanEqual18.checkAuthorized(request17))
        Assertions.assertTrue(lessThanEqual18.checkAuthorized(request18))
        Assertions.assertFalse(lessThanEqual18.checkAuthorized(request19))

    }

    @Test
    fun compareSubjectToResource() {
        val request17 = accessRequest().copy(
                subject = mapOf(Pair("age", 17)),
                resource = mapOf(Pair("limit", 18))
        )
        val request18 = accessRequest().copy(
                subject = mapOf(Pair("age", 18)),
                resource = mapOf(Pair("limit", 18))
        )

        val mustBeOverResourceLimitPolicy = Expression().subject("age").greaterThanEqual().resource("limit")

        Assertions.assertTrue(mustBeOverResourceLimitPolicy.checkAuthorized(request18))
        Assertions.assertFalse(mustBeOverResourceLimitPolicy.checkAuthorized(request17))
    }

    @Test
    fun compareResourceToEnvironment() {
        val expired = accessRequest().copy(
                resource = mapOf(Pair("expires", LocalDateTime.now().minusHours(2))),
                environment = mapOf(Pair("timeOfRequest", LocalDateTime.now()))
        )
        val notExpired = accessRequest().copy(
                resource = mapOf(Pair("expires", LocalDateTime.now().plusHours(2))),
                environment = mapOf(Pair("timeOfRequest", LocalDateTime.now()))
        )

        val mustNotBeExpiredPolicy = Expression().resource("expires").greaterThanEqual().environment("timeOfRequest")

        Assertions.assertFalse(mustNotBeExpiredPolicy.checkAuthorized(expired))
        Assertions.assertTrue(mustNotBeExpiredPolicy.checkAuthorized(notExpired))
    }

    @Test
    fun compareEnvironmentToSubject() {
        val notOnSite = accessRequest().copy(
                subject = mapOf(Pair("siteID", 3)),
                environment = mapOf(Pair("requestSiteID", 4))
        )
        val onSite = accessRequest().copy(
                subject = mapOf(Pair("siteID", 3)),
                environment = mapOf(Pair("requestSiteID", 3))
        )

        val subjectMustBeOnTheirSitePolicy = Expression().environment("requestSiteID").equalTo().subject("siteID")

        Assertions.assertFalse(subjectMustBeOnTheirSitePolicy.checkAuthorized(notOnSite))
        Assertions.assertTrue(subjectMustBeOnTheirSitePolicy.checkAuthorized(onSite))
    }

}

class ExpressionPolicyCollectionOperatorsTest {

    @Test
    fun checkAttributeContainsAllOf() {
        val allRoles = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER", "ACCOUNT_MANAGER", "SUPERVISOR")))
        )
        val someRoles = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER", "ACCOUNT_MANAGER")))
        )
        val noRoles = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER")))
        )

        val accountManagerPolicy = Expression().subject("roles").containsAll().value(listOf("ACCOUNT_MANAGER", "SUPERVISOR"))
        Assertions.assertTrue(accountManagerPolicy.checkAuthorized(allRoles))
        Assertions.assertFalse(accountManagerPolicy.checkAuthorized(someRoles))
        Assertions.assertFalse(accountManagerPolicy.checkAuthorized(noRoles))
    }

    @Test
    fun checkAttributeContainsAnyOf() {
        val managerRequest = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER", "ACCOUNT_MANAGER")))
        )
        val supervisorRequest = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("ADMIN", "SUPERVISOR")))
        )
        val neitherRequest = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER", "ADMIN")))
        )

        val supervisorOrManagerPolicy = Expression().subject("roles").containsAny().value(listOf("ACCOUNT_MANAGER", "SUPERVISOR"))
        Assertions.assertTrue(supervisorOrManagerPolicy.checkAuthorized(managerRequest))
        Assertions.assertTrue(supervisorOrManagerPolicy.checkAuthorized(supervisorRequest))
        Assertions.assertFalse(supervisorOrManagerPolicy.checkAuthorized(neitherRequest))
    }

    @Test
    fun checkAttributeContains(){
        val managerRequest = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER", "ACCOUNT_MANAGER")))
        )
        val supervisorRequest = accessRequest().copy(
                subject = mapOf(Pair("roles", listOf("USER", "SUPERVISOR")))
        )
        val managerPolicy = Expression().subject("roles").contains().value("ACCOUNT_MANAGER")

        Assertions.assertTrue(managerPolicy.checkAuthorized(managerRequest))
        Assertions.assertFalse(managerPolicy.checkAuthorized(supervisorRequest))
    }
}