package com.generalov.lab4.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.datastore.PreferencesManager
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

    init {
        repository = UserRepository(AppDatabase.getDatabase(application).userDao())
        preferencesManager = PreferencesManager(application)
        val userId: Int = preferencesManager.getUserId()
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            val user = repository.getUserById(id = userId)
            _user.value = user
        }
    }

    fun logout() {
        preferencesManager.removeUserId()
        _user.value = null
    }
}