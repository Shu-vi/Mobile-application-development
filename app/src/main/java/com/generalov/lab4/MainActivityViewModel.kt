package com.generalov.lab4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.generalov.lab4.datastore.PreferencesManager
import com.generalov.lab4.usecases.JwtService

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager: PreferencesManager
    private val jwtService: JwtService

    init {
        preferencesManager = PreferencesManager(application)
        jwtService = JwtService()
    }

    fun getUserId(): Int? {
        val token = preferencesManager.getToken()
        var userId: Int? = null
        if (token != null) {
            userId = jwtService.verifyToken(token)?.toInt()
        }
        return userId
    }
}