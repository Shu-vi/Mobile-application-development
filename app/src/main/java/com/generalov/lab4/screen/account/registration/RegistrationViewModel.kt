package com.generalov.lab4.screen.account.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.types.InputResult
import com.generalov.lab4.usecases.Validator
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
        val usernameValidated = Validator.usernameValidate(username)
        val phoneValidated = Validator.phoneValidate(phoneNumber)
        val passwordValidated = Validator.passwordValidate(password, confirmPassword)

        when (usernameValidated) {
            InputResult.FieldLong -> _registrationState.value = RegistrationState.UsernameLong
            InputResult.FieldShort -> _registrationState.value = RegistrationState.UsernameShort
            else -> {}
        }

        when (phoneValidated) {
            InputResult.FieldShort -> _registrationState.value = RegistrationState.PhoneNumberShort
            InputResult.FieldIncorrect -> _registrationState.value =
                RegistrationState.PhoneNumberIncorrect
            else -> {}
        }

        when (passwordValidated) {
            InputResult.FieldShort -> _registrationState.value = RegistrationState.PasswordShort
            InputResult.FieldLong -> _registrationState.value = RegistrationState.PasswordLong
            InputResult.FieldDoNotMatch -> _registrationState.value =
                RegistrationState.PasswordsDoNotMatch
            InputResult.FieldEmpty -> _registrationState.value = RegistrationState.PasswordEmpty
            else -> {}
        }

        if (passwordValidated == InputResult.Success &&
            phoneValidated == InputResult.Success &&
            usernameValidated == InputResult.Success
        ) {
            val user = User(null, username, password, phoneNumber)
            insert(user)
            _registrationState.value = RegistrationState.RegistrationSuccess
        }
    }

    private fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }
}

enum class RegistrationState {
    UsernameEmpty,
    UsernameShort,
    UsernameLong,
    PhoneNumberShort,
    PhoneNumberIncorrect,
    PasswordEmpty,
    PasswordsDoNotMatch,
    PasswordShort,
    PasswordLong,
    RegistrationSuccess,
    Initial
}