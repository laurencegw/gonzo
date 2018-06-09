package com.binarymonks.gonzo.web.controllers

import com.binarymonks.gonzo.core.common.Credentials
import org.springframework.security.core.context.SecurityContextHolder

fun getCredentials() = SecurityContextHolder.getContext().authentication.credentials as Credentials
