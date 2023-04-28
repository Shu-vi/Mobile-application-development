package com.generalov.lab4.screen.account.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.datastore.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val preferences: PreferencesManager
    private val _loginState = MutableStateFlow(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        preferences = PreferencesManager(application)
    }

    fun login(phoneNumber: String, password: String) {
        when {
            phoneNumber.isEmpty() -> _loginState.value = LoginState.PhoneNumberEmpty
            password.isEmpty() -> _loginState.value = LoginState.PasswordEmpty
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val user = repository.getUserByPhone(phoneNumber)
                    if (user != null && user.password == password) {
                        _loginState.value = LoginState.LoginSuccess
                        user.id?.let {
                            preferences.saveUserId(it)
                        }
                    } else {
                        _loginState.value = LoginState.InvalidCredentials
                    }
                }
            }
        }
    }
}

enum class LoginState {
    PhoneNumberEmpty,
    PasswordEmpty,
    InvalidCredentials,
    LoginSuccess,
    Initial
}