package com.generalov.lab4.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.datastore.PreferencesManager
import com.generalov.lab4.types.JwtState
import com.generalov.lab4.usecases.JwtService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val preferencesManager: PreferencesManager

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val jwtService: JwtService

    private val _jwtState = MutableStateFlow(JwtState.Init)
    val jwtState: StateFlow<JwtState> = _jwtState

    private val _users = MutableStateFlow(mutableListOf<User>())
    val users: StateFlow<List<User>> = _users

    init {
        repository = UserRepository(AppDatabase.getDatabase(application).userDao())
        preferencesManager = PreferencesManager(application)
        jwtService = JwtService()
        val accessToken = preferencesManager.getAccessToken()
        val refreshToken = preferencesManager.getRefreshToken()
        val userId: String? = jwtService.checkTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            preferencesManager = preferencesManager
        )
        if (userId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(1000)
                val user = userId.let { repository.getUserById(id = it.toInt()) }
                _user.value = user
                if (user != null) {
                    if (user.isAdmin) {
                        _users.value = repository.getUsers(userId.toInt()) as MutableList<User>
                    }
                }
            }
        } else {
            _jwtState.value = JwtState.JwtNull
        }
    }

    fun roleChange(user: User) {
        val accessToken = preferencesManager.getAccessToken()
        val refreshToken = preferencesManager.getRefreshToken()
        val userId: String? = jwtService.checkTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            preferencesManager = preferencesManager
        )
        if (userId != null){
            viewModelScope.launch(Dispatchers.IO) {
                user.isAdmin = !user.isAdmin
                repository.update(user)
            }
        } else {
            _jwtState.value = JwtState.JwtNull
        }

    }

    fun logout() {
        preferencesManager.removeAccessToken()
        preferencesManager.removeRefreshToken()
        _user.value = null
    }
}