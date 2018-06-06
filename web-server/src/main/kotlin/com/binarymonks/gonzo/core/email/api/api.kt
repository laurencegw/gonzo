package com.binarymonks.gonzo.core.email.api

import com.binarymonks.gonzo.core.users.api.ResetToken

interface Emails {
    fun sendResetPassword(resetPasswordEmail: ResetPasswordEmail)
    fun sendUnknownUserPasswordReset(email: String)
}

data class ResetPasswordEmail(
        val emailAddress: String,
        val resetToken: ResetToken
)