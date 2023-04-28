package com.generalov.lab4.screen.account.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegistrationPage() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+7") }
    var passwordError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var resultInfo by remember { mutableStateOf("") }
    val viewModel: RegistrationViewModel = viewModel()
    val registrationState by viewModel.registrationState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    when (registrationState) {
        RegistrationState.UsernameEmpty -> usernameError = "Поле не может быть пустым"
        RegistrationState.UsernameShort -> usernameError =
            "Имя пользователя слишком короткое(не менее 3 символов)"
        RegistrationState.UsernameLong -> usernameError =
            "Имя пользователя не может быть таким длинным(не более 20 символов)"
        RegistrationState.PasswordEmpty -> passwordError = "Поле не может быть пустым"
        RegistrationState.PasswordsDoNotMatch -> passwordError = "Пароли не совпадают"
        RegistrationState.PasswordShort -> passwordError =
            "Пароль слишком короткий(не менее 5 символов)"
        RegistrationState.PasswordLong -> passwordError =
            "Пароль слишком длинный(не более 16 символов)"
        RegistrationState.PhoneNumberShort -> phoneError = "Телефон слишком короткий"
        RegistrationState.PhoneNumberIncorrect -> phoneError = "Разрешены только цифры"
        RegistrationState.RegistrationSuccess -> {
            resultInfo = "Регистрация прошла успешно"
            username = ""
            password = ""
            repeatPassword = ""
            phone = ""
            LaunchedEffect(Unit) {
                delay(3000)
                coroutineScope.launch {
                    resultInfo = ""
                }
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

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Имя пользователя") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = phone,
                onValueChange = { value ->
                    if (value.length in 2..12 && value.startsWith("+7")) {
                        phone = value
                    }
                },
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = phoneError,
                color = MaterialTheme.colors.error
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = passwordError,
                color = MaterialTheme.colors.error
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Повторите пароль") },
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
                }

            ) {
                Text("Зарегистрироваться")
            }
            Text(text = resultInfo)
        }
    }
}