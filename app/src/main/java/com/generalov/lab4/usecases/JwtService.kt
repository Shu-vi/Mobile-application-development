package com.generalov.lab4.usecases


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.generalov.lab4.datastore.PreferencesManager
import java.util.*

class JwtService {
    private val refreshSecret = "refreshSecret"
    private val accessSecret = "accessSecret"
    private val issuer = "Generalov"


    // Проверяем токен и возвращаем id пользователя, если все прошло успешно
    private fun verifyAccessToken(token: String): String? {
        val algorithm = Algorithm.HMAC256(accessSecret)
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
        return try {
            val decodedToken = verifier.verify(token)
            decodedToken.subject
        } catch (e: Exception) {
            null
        }
    }

    private fun verifyRefreshToken(refreshToken: String): String? {
        val algorithm = Algorithm.HMAC256(refreshSecret)
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
        return try {
            val decodedToken = verifier.verify(refreshToken)
            decodedToken.subject
        } catch (e: Exception) {
            null
        }
    }

    fun generateTokens(userId: Int): Pair<String, String> {
        val accessToken = generateAccessToken(userId)

        val refreshToken = generateRefreshToken(userId)

        return Pair(accessToken, refreshToken)
    }

    private fun generateAccessTokenFromRefreshToken(refreshToken: String): String? {
        val userId = verifyRefreshToken(refreshToken) ?: return null
        return generateAccessToken(userId.toInt())
    }


    private fun generateRefreshToken(userId: Int): String {
        val algorithm = Algorithm.HMAC256(refreshSecret)
        val nowMillis = System.currentTimeMillis()
        val expiresAt = Date(nowMillis + 1000 * 60 * 2) // 2 минуты
        return JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(expiresAt)
            .withSubject(userId.toString())
            .sign(algorithm)
    }

    private fun generateAccessToken(userId: Int): String {
        val algorithm = Algorithm.HMAC256(accessSecret)
        val nowMillis = System.currentTimeMillis()
        val expiresAt = Date(nowMillis + 1000 * 60) // 1 минута
        return JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(expiresAt)
            .withSubject(userId.toString())
            .sign(algorithm)
    }

    fun checkTokens(
        accessToken: String?,
        refreshToken: String?,
        preferencesManager: PreferencesManager
    ): String? {
        var userId: String? = null
        if (accessToken != null) {
            userId = verifyAccessToken(accessToken)
        }
        if (userId == null && refreshToken != null) {
            val newAccessToken = generateAccessTokenFromRefreshToken(refreshToken)
            if (newAccessToken != null) {
                preferencesManager.removeAccessToken()
                preferencesManager.saveAccessToken(newAccessToken)
                userId = verifyAccessToken(newAccessToken)
            }
        }
        return userId
    }

}
