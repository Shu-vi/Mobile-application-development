package com.generalov.lab4.screen.profile

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
import com.generalov.lab4.components.TextFieldWithError
import com.generalov.lab4.screen.Screen
import com.generalov.lab4.types.InputResult

@Composable
fun ProfilePage(navController: NavHostController) {
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsState()

    if (user != null) {
        var username by remember { mutableStateOf(user!!.username) }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        var passwordError by remember { mutableStateOf("") }
        var usernameError by remember { mutableStateOf("") }
        var res by remember { mutableStateOf("") }

        val fieldsState by viewModel.fieldsState.collectAsState()
        val profileState by viewModel.profileState.collectAsState()

        var isPasswordCleared by remember { mutableStateOf(false) }


        res = when (profileState) {
            ProfileState.DataUpdateSuccess -> "Данные успешно сохранены"
            else -> ""
        }

        when (fieldsState.passwordState) {
            InputResult.FieldDoNotMatch -> passwordError = "Пароли не совпадают"
            InputResult.FieldShort -> passwordError = "Пароль слишком короткий(не менее 5 символов)"
            InputResult.FieldLong -> passwordError = "Пароль слишком длинный(не более 16 символов)"
            InputResult.Success -> {
                if (!isPasswordCleared) {
                    password = ""
                    confirmPassword = ""
                    passwordError = ""
                    isPasswordCleared = true
                }
            }
            else -> passwordError = ""
        }

        usernameError = when (fieldsState.usernameState) {
            InputResult.FieldEmpty -> "Поле не может быть пустым"
            InputResult.FieldShort -> "Имя пользователя слишком короткое(не менее 3 символов)"
            InputResult.FieldLong -> "Имя пользователя не может быть таким длинным(не более 20 символов)"
            else -> ""
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

            TextFieldWithError(
                label = "Имя пользователя",
                value = username,
                onValueChange = { value ->
                    username = value
                },
                errorMessage = usernameError,
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
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Повторите пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.updatePasswordAndUsername(username, password, confirmPassword)
                    isPasswordCleared = false
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Yellow,
                    contentColor = Color.Black
                ),
                enabled = password.isNotEmpty() || username != user!!.username
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