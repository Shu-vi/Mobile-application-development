package com.generalov.lab4.screen.account.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    private val _registrationState = MutableStateFlow(RegistrationState.Initial)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun register(username: String, phoneNumber: String, password: String, confirmPassword: String) {
        when {
            username.isEmpty() -> {
                _registrationState.value = RegistrationState.UsernameEmpty
            }
            phoneNumber.isEmpty() -> {
                _registrationState.value = RegistrationState.PhoneNumberEmpty
            }
            password.isEmpty() -> {
                _registrationState.value = RegistrationState.PasswordEmpty
            }
            password != confirmPassword -> {
                _registrationState.value = RegistrationState.PasswordsDoNotMatch
            }
            phoneNumber.length < 12 -> {
                _registrationState.value = RegistrationState.PhoneNumberShort
            }
            else -> {
                val user = User(null, username, password, phoneNumber)
                insert(user)
                _registrationState.value = RegistrationState.RegistrationSuccess
            }
        }
    }

    private fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}

enum class RegistrationState {
    UsernameEmpty,
    PhoneNumberEmpty,
    PhoneNumberShort,
    PasswordEmpty,
    PasswordsDoNotMatch,
    RegistrationSuccess,
    Initial
}