package com.generalov.lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.generalov.lab3.database.entity.User
import com.generalov.lab3.view.UserViewModel

class ProfileActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfilePage()
        }
    }

    @Composable
    fun ProfilePage() {
        var user: User = intent.getSerializableExtra("user") as User
        var username by remember { mutableStateOf(user.username) }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf("") }
        var usernameError by remember { mutableStateOf("") }
        var res by remember { mutableStateOf("") }
        val users by userViewModel.user.observeAsState()
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
                        passwordError = "";
                        usernameError = "";
                        res = "";
                        val intent = Intent(ProfileActivity@this@ProfileActivity, HomeActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
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
                value = user.phone,
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
                    var err: Boolean = false;
                    passwordError = "";
                    usernameError = "";
                    res = "";
                    if (password != confirmPassword) {
                        passwordError = "Пароли не совпадают";
                        err = true;
                    }


                    if (username == "") {
                        usernameError = "Поле не может быть пустым"
                        err = true
                    }
                    if (err) {
                        return@Button
                    }
                    if (password != ""){
                        user.password = password
                    }
                    user.username = username
                    try {
                        print(user.toString())
                        userViewModel.updateUser(user);
                        if (users != null){
                            user = users?.first()!!
                        }
                        res = "Данные сохранены " + user.toString()
                    } catch (_: Exception) {
                        res = "Что-то пошло не так"
                    }
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
    }
}

