package com.binarymonks.gonzo.core.users.service

import com.binarymonks.gonzo.core.common.ExpiredToken
import com.binarymonks.gonzo.core.common.InvalidCredentials
import com.binarymonks.gonzo.core.time.nowUTC
import com.binarymonks.gonzo.core.users.api.LoginCredentials
import com.binarymonks.gonzo.core.users.api.SignIn
import com.binarymonks.gonzo.core.users.api.User
import com.binarymonks.gonzo.core.users.persistence.UserRepo
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.impl.crypto.MacProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.*
import java.util.*


@Service
class SignInService : SignIn {
    var clock: Clock = java.time.Clock.systemUTC()

    // TODO: put this in external config
    val key = MacProvider.generateKey()
    val tokenLifeSpan = Duration.ofHours(2)!!

    @Autowired
    lateinit var userRepo: UserRepo
    @Autowired
    lateinit var passwords: Passwords

    override fun login(credentials: LoginCredentials): String {
        val userEntity = userRepo.findByEmail(credentials.email).get()
        val passwordAttempt = passwords.hashPassword(
                password = credentials.password,
                salt = userEntity.spice.pepper
        )
        if (passwordAttempt != userEntity.encryptedPassword) {
            throw InvalidCredentials()
        }
        val expiry= nowUTC(clock).plus(tokenLifeSpan)
        val jwts: JwtBuilder = Jwts.builder()
                .setExpiration(Date(expiry.toInstant().toEpochMilli()))
                .setSubject(userEntity.email)
                .signWith(SignatureAlgorithm.HS512, key)
        return jwts.compact()
    }

    override fun assertLoggedIn(token: String) {
        getUserFromToken(token)
    }

    override fun getUserFromToken(token: String): User {
        try {
            val parsedToken = Jwts.parser().setSigningKey(key).parseClaimsJws(token)
            val userEmail: String = parsedToken.getBody().getSubject()
            val expiryDate = parsedToken.getBody().get("exp", Date::class.java)
            val now = nowUTC(clock)
            if(now.isAfter(ZonedDateTime.ofInstant(expiryDate.toInstant(), ZoneId.of("UTC")))){
                throw ExpiredToken()
            }
            return userRepo.findByEmail(userEmail).get().toUser()
        } catch (e: SignatureException) {
            throw InvalidCredentials()
        }
    }


}