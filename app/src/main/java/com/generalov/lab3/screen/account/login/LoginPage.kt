package com.generalov.lab3.screen.account.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.generalov.lab3.screen.Screen

@Composable
fun LoginPage(navController: NavHostController) {
    var phone by remember { mutableStateOf("+7") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var res by remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        LoginState.PhoneNumberEmpty -> phoneError = "Поле не может быть пустым"
        LoginState.PasswordEmpty -> passwordError = "Поле не может быть пустым"
        LoginState.InvalidCredentials -> res = "Неправильный логин или пароль"
        LoginState.LoginSuccess -> {
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

            TextField(value = phone, onValueChange = { value ->
                if (value.length in 2..12 && value.startsWith("+7")) {
                    phone = value
                }
            }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth()
            )
            Text(text = phoneError)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Text(text = passwordError)

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