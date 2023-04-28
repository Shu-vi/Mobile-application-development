package com.generalov.lab4.screen.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.datastore.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val preferencesManager: PreferencesManager
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    private val _profileState = MutableStateFlow(ProfileState.Initial)
    val profileState: StateFlow<ProfileState> = _profileState

    init {
        repository = UserRepository(AppDatabase.getDatabase(application).userDao())
        preferencesManager = PreferencesManager(application)
        val userId: Int = preferencesManager.getUserId()
        viewModelScope.launch(Dispatchers.IO) {
//            delay(1000)
            val user = repository.getUserById(id = userId)
            _user.value = user
        }
    }

    fun updateUsername(username: String) {
        if (username.isEmpty()) {
            _profileState.value = ProfileState.UsernameEmpty
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = _user.value
            if (user != null) {
                user.username = username
                repository.update(user)
                _profileState.value = ProfileState.DataUpdateSuccess
            }
        }
    }

    fun updatePassword(password: String, confirmPassword: String) {
        if (password.isEmpty()) {
            _profileState.value = ProfileState.PasswordEmpty
            return
        }
        if (password != confirmPassword) {
            _profileState.value = ProfileState.PasswordsDoNotMatch
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = _user.value
            if (user != null) {
                user.password = password
                repository.update(user)
                _profileState.value = ProfileState.DataUpdateSuccess
            }
        }
    }
}

enum class ProfileState {
    UsernameEmpty,
    PasswordEmpty,
    PasswordsDoNotMatch,
    DataUpdateSuccess,
    Initial
}