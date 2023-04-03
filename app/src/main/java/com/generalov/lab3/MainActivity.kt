package com.generalov.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Account();
        }
    }
}


@Composable
fun Account() {
    var tabPage by remember {
        mutableStateOf(TabPage.Authorization)
    }
    Scaffold(topBar = {
        TabHome(
            selectedTabIndex = tabPage.ordinal,
            onSelectedTab = { tabPage = it })
    },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                if (tabPage.name == "Authorization") {
                    LoginPage();
                } else {
                    RegistrationPage();
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPage() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Login icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Авторизация",
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            ) {
                Text("Войти")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPage() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Registration icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(64.dp)
            )
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
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
                onClick = {}
            ) {
                Text("Зарегистрироваться")
            }
        }
    }
}
