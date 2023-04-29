package com.generalov.lab4.screen.account.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegistrationPage() {
    val viewModel: RegistrationViewModel = viewModel()
    val registrationState by viewModel.registrationState.collectAsState()

    val resources = LocalContext.current.resources
    val usernameError = when (registrationState) {
        RegistrationState.UsernameEmpty -> "Поле не может быть пустым"
        RegistrationState.UsernameShort -> "Имя пользователя слишком короткое(не менее 3 символов)"
        RegistrationState.UsernameLong -> "Имя пользователя не может быть таким длинным(не более 20 символов)"
        else -> ""
    }

    val passwordError = when (registrationState) {
        RegistrationState.PasswordEmpty -> "Поле не может быть пустым"
        RegistrationState.PasswordsDoNotMatch -> "Пароли не совпадают"
        RegistrationState.PasswordShort -> "Пароль слишком короткий(не менее 5 символов)"
        RegistrationState.PasswordLong -> "Пароль слишком длинный(не более 16 символов)"
        else -> ""
    }

    val phoneError = when (registrationState) {
        RegistrationState.PhoneNumberShort -> "Телефон должен состоять из 11 цифр"
        RegistrationState.PhoneNumberIncorrect -> "Поле должно содержать только цифры"
        else -> ""
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+7") }
    var resultInfo by remember { mutableStateOf("") }

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

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("") },
                isError = usernameError.isNotEmpty(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (usernameError.isNotEmpty()) {
                Text(
                    text = usernameError,
                    color = MaterialTheme.colors.error
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { value ->
                    if (value.length in 2..12 && value.startsWith("+7")) {
                        phone = value
                    }
                },
                label = { Text("Номер телефона") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                ),
                isError = phoneError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = phoneError,
                color = MaterialTheme.colors.error
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = passwordError,
                color = MaterialTheme.colors.error
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Повторите пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError.isNotEmpty() && repeatPassword.isNotEmpty() && repeatPassword != password
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    passwordError = ""
                    usernameError = ""
                    phoneError = ""
                    viewModel.register(
                        username = username,
                        phoneNumber = phone,
                        password = password,
                        confirmPassword = repeatPassword
                    )
                    username = ""
                    password = ""
                    repeatPassword = ""
                    phone = ""
                }

            ) {
                Text("Зарегистрироваться")
            }
            Text(text = resultInfo)
        }
    }
}