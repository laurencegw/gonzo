package com.binarymonks.gonzo.core.test.stubs

import com.binarymonks.gonzo.core.email.api.Emails
import com.binarymonks.gonzo.core.email.api.ResetPasswordEmail
import org.springframework.stereotype.Service

@Service
class StubEmailService : Emails {

    val resetPasswordInbox = mutableListOf<ResetPasswordEmail>()
    val resetUnknownInbox = mutableListOf<String>()

    override fun sendResetPassword(resetPasswordEmail: ResetPasswordEmail) {
        resetPasswordInbox.add(resetPasswordEmail)
    }

    override fun sendUnknownUserPasswordReset(email: String) {
        resetUnknownInbox.add(email)
    }

    fun clearInbox(){
        resetUnknownInbox.clear()
        resetPasswordInbox.clear()
    }

}