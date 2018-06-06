package com.binarymonks.gonzo.core.email.service

import com.binarymonks.gonzo.core.email.api.Emails
import com.binarymonks.gonzo.core.email.api.ResetPasswordEmail
import org.springframework.stereotype.Service

@Service
class EmailService : Emails {
    override fun sendResetPassword(resetPasswordEmail: ResetPasswordEmail) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendUnknownUserPasswordReset(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}