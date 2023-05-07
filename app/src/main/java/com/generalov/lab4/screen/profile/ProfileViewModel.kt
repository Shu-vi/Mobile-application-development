package com.generalov.lab4.screen.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.generalov.lab4.database.AppDatabase
import com.generalov.lab4.database.entity.User
import com.generalov.lab4.database.repo.UserRepository
import com.generalov.lab4.datastore.PreferencesManager
import com.generalov.lab4.types.InputResult
import com.generalov.lab4.usecases.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private val _fieldsState =
        MutableStateFlow(FieldsState(InputResult.Initial, InputResult.Initial))
    val fieldsState: StateFlow<FieldsState> = _fieldsState

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

    fun updatePasswordAndUsername(username: String, password: String, confirmPassword: String) {
        val usernameValidated = Validator.usernameValidate(username)
        val passwordValidated = Validator.passwordValidate(password, confirmPassword)
        _fieldsState.value = FieldsState(passwordValidated, usernameValidated)
        if (usernameValidated == InputResult.Success && passwordValidated == InputResult.Success) {
            updatePassword(password)
            updateUsername(username)
            _profileState.value = ProfileState.DataUpdateSuccess
        } else if (usernameValidated == InputResult.Success && passwordValidated == InputResult.FieldEmpty){
            updateUsername(username)
            _profileState.value = ProfileState.DataUpdateSuccess
        } else {
            _profileState.value = ProfileState.Initial
        }

    }

    private fun updateUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = _user.value
            if (user != null) {
                user.username = username
                repository.update(user)
            }
        }
    }

    private fun updatePassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = _user.value
            if (user != null) {
                user.password = password
                repository.update(user)
            }
        }
    }
}

enum class ProfileState {
    DataUpdateSuccess,
    Initial
}

data class FieldsState(
    val passwordState: InputResult,
    val usernameState: InputResult
)