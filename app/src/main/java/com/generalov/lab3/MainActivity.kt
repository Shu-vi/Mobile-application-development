package com.generalov.lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.generalov.lab3.database.entity.User
import com.generalov.lab3.view.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Account();
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Account() {
        val pagerState = rememberPagerState(pageCount = TabPage.values().size)
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                TabHome(
                    selectedTabIndex = pagerState.currentPage,
                    onSelectedTab = { scope.launch { pagerState.animateScrollToPage(it.ordinal) } })
            },
            content = { padding ->
                HorizontalPager(state = pagerState) { index ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        when (index) {
                            0 -> LoginPage()
                            1 -> RegistrationPage()
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun LoginPage() {
        var phone by remember { mutableStateOf("+7") }
        var password by remember { mutableStateOf("") }
        val user by userViewModel.user.observeAsState()
        var res by remember { mutableStateOf("") }
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
                    value = phone,
                    onValueChange = { value ->
                        if (value.length > 1 && value.length < 13 && value.startsWith("+7")) {
                            phone = value;
                        }
                    },
                    label = { Text("Телефон") },
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
                    onClick = {
                        val foundUser =
                            user?.firstOrNull { it.phone == phone && it.password == password }
                        if (foundUser != null) {
                            // авторизация прошла успешно
                            val intent =
                                Intent(MainActivity@ this@MainActivity, HomeActivity::class.java)
                            intent.putExtra("user", foundUser)
                            startActivity(intent)
                        } else {
                            // неверный телефон или пароль
                            res = "неверный телефон или пароль"
                        }
                    }
                ) {
                    Text("Войти")
                }
                Text(text = res)
            }
        }
    }

    @Composable
    fun RegistrationPage() {
        var username: MutableState<String> = remember { mutableStateOf("") }
        var password: MutableState<String> = remember { mutableStateOf("") }
        var repeatPassword: MutableState<String> = remember { mutableStateOf("") }
        var phone: MutableState<String> = remember { mutableStateOf("+7") }
        var passwordError: MutableState<String> = remember { mutableStateOf("") }
        var phoneError: MutableState<String> = remember { mutableStateOf("") }
        var resultInfo: MutableState<String> = remember { mutableStateOf("") }


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
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Имя пользователя") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = phone.value,
                    onValueChange = { value ->
                        if (value.length > 1 && value.length < 13 && value.startsWith("+7")) {
                            phone.value = value;
                        }
                    },
                    label = { Text("Номер телефона") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = phoneError.value,
                    color = MaterialTheme.colors.error
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Пароль") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Text(
                    text = passwordError.value,
                    color = MaterialTheme.colors.error
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = repeatPassword.value,
                    onValueChange = { repeatPassword.value = it },
                    label = { Text("Повторите пароль") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onRegisterClick(
                            userViewModel,
                            username,
                            phone,
                            password,
                            repeatPassword,
                            passwordError,
                            phoneError,
                            resultInfo
                        )
                    }

                ) {
                    Text("Зарегистрироваться")
                }
                Text(text = resultInfo.value)
            }
        }
    }

    fun onRegisterClick(
        userViewModel: UserViewModel,
        username: MutableState<String>,
        phone: MutableState<String>,
        password: MutableState<String>,
        repeatPassword: MutableState<String>,
        passwordError: MutableState<String>,
        phoneError: MutableState<String>,
        resultInfo: MutableState<String>
    ) {
        var err: Boolean = false;
        val regex = """^\+7\d{10}$""".toRegex()
        resultInfo.value = "";
        phoneError.value = "";
        passwordError.value = "";
        if (password.value != repeatPassword.value) {
            passwordError.value = "Пароли не совпадают";
            err = true;
        }

        if (!phone.value.matches(regex)) {
            phoneError.value = "Разрешены только цифры"
            err = true
        }
        if (err) {
            return
        }

        var user: User = User(username.value, password.value, phone.value, null);
        try {
            userViewModel.saveUser(user);
            resultInfo.value = "Регистрация прошла успешно";
            username.value = "";
            phone.value = "+7";
            password.value = "";
            repeatPassword.value = "";
        } catch (e: Exception) {
            resultInfo.value = "Пользователь с таким номером телефона уже зарегистрирован"
        }

    }
}