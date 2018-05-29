package com.binarymonks.gonzo.core.authz.service

import com.binarymonks.gonzo.core.authz.api.AccessDecider
import com.binarymonks.gonzo.core.authz.api.AccessRequest

class AccessDecisionService(var policies: List<AccessDecider>): AccessDecider{

    override fun checkAuthorized(accessRequest: AccessRequest):Boolean {
        for (policy in policies){
            if(policy.checkAuthorized(accessRequest)){
                return true
            }
        }
        return false
    }

}