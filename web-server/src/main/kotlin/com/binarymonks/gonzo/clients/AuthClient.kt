package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.users.api.LoginCredentials
import com.binarymonks.gonzo.web.Routes
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod


abstract class AuthClient(internal val baseURL: String) {
    internal val restTemplate = restTemplateWithErrorHandler()
    private var token: String = ""

    internal fun createHeaders(): HttpHeaders {
        return object : HttpHeaders() {
            init {
                val authHeader = "Bearer " + token
                set("Authorization", authHeader)
            }
        }
    }

    fun signIn(email: String, password: String) {
        val response = restTemplate.exchange(
                "$baseURL/${Routes.LOGIN}",
                HttpMethod.POST,
                HttpEntity(LoginCredentials(
                        email = email,
                        password = password
                )),
                object : ParameterizedTypeReference<String>() {}
        )
        token = response.body
    }
}