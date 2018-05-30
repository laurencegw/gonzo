package com.binarymonks.gonzo.core.authz.service

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest

class StubAccessDecider(val requestToMatch: AccessRequest): AccessDecider {
    override fun checkAuthorized(accessRequest: AccessRequest): Boolean {
        return requestToMatch == accessRequest
    }

}