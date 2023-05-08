package com.generalov.lab4.usecases


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtService {

    // Проверяем токен и возвращаем id пользователя, если все прошло успешно
    fun verifyToken(token: String): String? {
        val algorithm = Algorithm.HMAC256("secret")
        val verifier = JWT.require(algorithm)
            .withIssuer("myApp")
            .build()
        return try {
            val decodedToken = verifier.verify(token)
            decodedToken.subject
        } catch (e: Exception) {
            null
        }
    }

    // Генерируем токен
    fun generateToken(userId: Int): String {
        val algorithm = Algorithm.HMAC256("secret")
        val nowMillis = System.currentTimeMillis()
        val expiresAt = Date(nowMillis + 60_000) // 1 час
        return JWT.create()
            .withIssuer("myApp")
            .withExpiresAt(expiresAt)
            .withSubject(userId.toString())
            .sign(algorithm)
    }
}
