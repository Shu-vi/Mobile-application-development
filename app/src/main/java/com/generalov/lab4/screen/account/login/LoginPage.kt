package com.generalov.lab4.screen.account.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.generalov.lab4.components.TextFieldWithError
import com.generalov.lab4.screen.Screen
import com.generalov.lab4.types.InputResult

@Composable
fun LoginPage(navController: NavHostController) {
    var phone by remember { mutableStateOf("+7") }
    var password by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var res by remember { mutableStateOf("") }

    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState.collectAsState()
    val fieldsState by viewModel.fieldState.collectAsState()

    phoneError = when (fieldsState.phoneState) {
        InputResult.Success -> ""
        InputResult.FieldShort -> "Телефон слишком короткий"
        InputResult.FieldIncorrect -> "Телефон может содержать только цифры"
        else -> ""
    }

    passwordError = when (fieldsState.passwordState) {
        InputResult.Success -> ""
        InputResult.FieldShort -> "Пароль слишком короткий"
        InputResult.FieldDoNotMatch -> "Пароли не совпадают"
        InputResult.FieldEmpty -> "Поле не может быть пустым"
        InputResult.FieldLong -> "Пароль слишком длинный"
        else -> ""
    }

    when (loginState) {
        LoginState.LoginIncorrect -> res = "Неверный логин или пароль"
        LoginState.LoginSuccess -> {
            res = ""
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Home.route)
            }
        }
        else -> {}
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Login icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Авторизация", style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithError(
                label = "Телефон",
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                viewModel.login(phone, password)
            }) {
                Text("Войти")
            }
            Text(text = res)
        }
    }
}