package com.generalov.lab3.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.generalov.lab3.screen.Screen

@Composable
fun ProfilePage(navController: NavHostController) {
    val viewModel: ProfileViewModel = viewModel()
    val profileState by viewModel.profileState.collectAsState()
    val user by viewModel.user.collectAsState()
    if (user != null) {
        var username by remember { mutableStateOf(user!!.username) }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf("") }
        var usernameError by remember { mutableStateOf("") }
        var res by remember { mutableStateOf("") }

        when (profileState) {
            ProfileState.PasswordEmpty -> passwordError = "Поле не может быть пустым"
            ProfileState.DataUpdateSuccess -> res = "Данные успешно сохранены"
            ProfileState.PasswordsDoNotMatch -> passwordError = "Пароли не совпадают"
            ProfileState.UsernameEmpty -> usernameError = "Поле не может быть пустым"
            else -> {}
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Yellow,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Назад")
                }
            }
            Text(
                text = "Изменить профиль",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = user!!.phone,
                onValueChange = {},
                label = { Text("Номер телефона") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = username,
                onValueChange = { value ->
                    username = value
                },
                label = { Text("Имя пользователя") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = usernameError)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Text(text = passwordError)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Повторите пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.updatePassword(password, confirmPassword)
                    viewModel.updateUsername(username)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Yellow,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Сохранить изменения")
            }
            Text(text = res)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}