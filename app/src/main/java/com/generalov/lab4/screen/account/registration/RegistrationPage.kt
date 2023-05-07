package com.generalov.lab4.screen.account.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.generalov.lab4.components.TextFieldWithError
import com.generalov.lab4.types.InputResult

@Composable
fun RegistrationPage() {
    val viewModel: RegistrationViewModel = viewModel()
    val registrationState by viewModel.registrationState.collectAsState()
    val fieldsState by viewModel.fieldsState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+7") }

    var resultInfo by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }

    var isPasswordCleared by remember { mutableStateOf(false) }
    var isUsernameCleared by remember { mutableStateOf(false) }
    var isPhoneCleared by remember { mutableStateOf(false) }

    usernameError = when (fieldsState.usernameState) {
        InputResult.FieldEmpty -> "Поле не может быть пустым"
        InputResult.FieldShort -> "Имя пользователя слишком короткое(не менее 3 символов)"
        InputResult.FieldLong -> "Имя пользователя не может быть таким длинным(не более 20 символов)"
        else -> ""
    }

    passwordError = when (fieldsState.passwordState) {
        InputResult.FieldEmpty -> "Поле не может быть пустым"
        InputResult.FieldDoNotMatch -> "Пароли не совпадают"
        InputResult.FieldShort -> "Пароль слишком короткий(не менее 5 символов)"
        InputResult.FieldLong -> "Пароль слишком длинный(не более 16 символов)"
        else -> ""
    }

    phoneError = when (fieldsState.phoneState) {
        InputResult.FieldShort -> "Телефон должен состоять из 11 цифр"
        InputResult.FieldIncorrect -> "Поле должно содержать только цифры"
        else -> ""
    }

    when (registrationState) {
        RegistrationState.RegistrationIncorrect -> {
            resultInfo = "Пользователь с таким телефоном уже существует"
            if (!isPasswordCleared) {
                password = ""
                repeatPassword = ""
                isPasswordCleared = true
            }
        }
        RegistrationState.RegistrationSuccess -> {
            resultInfo = "Регистрация прошла успешно"
            if (!isUsernameCleared) {
                username = ""
                isUsernameCleared = true
            }
            if (!isPasswordCleared) {
                password = ""
                repeatPassword = ""
                isPasswordCleared = true
            }
            if (!isPhoneCleared){
                phone = "+7"
                isPhoneCleared = true
            }
        }
        else -> {}
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithError(
                label = "Имя пользователя",
                value = username,
                onValueChange = { username = it },
                errorMessage = usernameError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextFieldWithError(
                label = "Номер телефона",
                value = phone,
                onValueChange = { value ->
                    if (value.length in 2..12 && value.startsWith("+7")) {
                        phone = value
                    }
                },
                errorMessage = phoneError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextFieldWithError(
                label = "Пароль",
                value = password,
                onValueChange = { password = it },
                errorMessage = passwordError,
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = {
                    Text(
                        text = "Повторите пароль"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.register(
                        username = username,
                        phoneNumber = phone,
                        password = password,
                        confirmPassword = repeatPassword
                    )
                    isPasswordCleared = false
                    isUsernameCleared = false
                    isPhoneCleared = false
                }

            ) {
                Text("Зарегистрироваться")
            }
            Text(text = resultInfo)
        }
    }
}