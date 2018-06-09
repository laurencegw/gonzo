package com.binarymonks.gonzo.web.filters

import com.binarymonks.gonzo.core.common.AnonymousCredentials
import com.binarymonks.gonzo.core.common.Credentials
import com.binarymonks.gonzo.core.common.TokenCredentials
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.service.SignInService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


const val AUTH_HEADER = "Authorization"
const val SECURITY_TYPE_PREFIX = "Bearer "

data class JWTAuthentication(
        private val credentials: Credentials,
        private val name: String? = null, // Do this in the application layer
        private val user: User? = null, // Do this in the application layer
        private val authorities: MutableCollection<out GrantedAuthority> = mutableListOf(),
        private val authenticated: Boolean = true
) : Authentication {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    override fun getName(): String? = name

    override fun getCredentials(): Any = credentials

    override fun getPrincipal(): Any? = user

    override fun getDetails(): Any? = null

    override fun isAuthenticated(): Boolean = authenticated
}

/**
 * A JWT Authentication Filter for [SignInService] based tokens.
 *
 * Cool article explaining this stuff here: https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 */
class JWTAuthenticationFilter(
        authManager: AuthenticationManager
) : BasicAuthenticationFilter(authManager) {


    override fun doFilterInternal(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val header: String? = request?.getHeader(AUTH_HEADER)
        if (header != null && header.startsWith(SECURITY_TYPE_PREFIX)) {
            val token = header.substringAfter(SECURITY_TYPE_PREFIX)
            SecurityContextHolder.getContext().authentication = JWTAuthentication(
                    TokenCredentials(token)
            )
        } else {
            SecurityContextHolder.getContext().authentication = JWTAuthentication(
                    AnonymousCredentials()
            )
        }
        chain?.doFilter(request, response)
    }
}
