package com.generalov.lab4.usecases

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.generalov.lab4.database.entity.User
import java.util.*

class AuthService {
    private val sercretKey = "secret"

    // Генерация токена на клиентской стороне
    private fun generateToken(user: User): String {
        val algorithm = Algorithm.HMAC256(sercretKey)
        val header = mapOf("alg" to "HS256")
        val expiresAt = Date(System.currentTimeMillis() + 60_000) // Токен действителен 1 минуту

        return JWT.create()
            .withHeader(header)
            .withClaim("id", user.id)
            .withClaim("isAdmin", user.isAdmin)
            .withExpiresAt(expiresAt)
            .sign(algorithm)
    }

    // Проверка токена на серверной стороне
    fun verifyToken(token: String): Boolean {
        val algorithm = Algorithm.HMAC256(sercretKey)
        return try {
            val verifier = JWT.require(algorithm).build()
            verifier.verify(token)
            true
        } catch (exception: JWTVerificationException) {
            false
        }
    }
}