package com.generalov.lab4.screen.account.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.screen.account.login.LoginState
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

    private val _fieldsState = MutableStateFlow(FieldsState(InputResult.Initial, InputResult.Initial, InputResult.Initial))
    val fieldsState: StateFlow<FieldsState> = _fieldsState

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun register(username: String, phoneNumber: String, password: String, confirmPassword: String) {
        val usernameValidated = Validator.usernameValidate(username)
        val phoneValidated = Validator.phoneValidate(phoneNumber)
        val passwordValidated = Validator.passwordValidate(password, confirmPassword)
        _fieldsState.value= FieldsState(phoneValidated, passwordValidated, usernameValidated)

        if (passwordValidated == InputResult.Success &&
            phoneValidated == InputResult.Success &&
            usernameValidated == InputResult.Success
        ) {
            var userFounded: User? = null
            viewModelScope.launch(Dispatchers.IO) {
                userFounded = repository.getUserByPhone(phoneNumber)
                if (userFounded != null) {
                    _registrationState.value = RegistrationState.RegistrationIncorrect
                } else{
                    val user = User(null, username, password, phoneNumber)
                    repository.insert(user)
                    _registrationState.value = RegistrationState.RegistrationSuccess
                }
            }
        }
    }
}

enum class RegistrationState {
    RegistrationSuccess,
    Initial,
    RegistrationIncorrect
}

data class FieldsState(
    val phoneState: InputResult,
    val passwordState: InputResult,
    val usernameState: InputResult
)